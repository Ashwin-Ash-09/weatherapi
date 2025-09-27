package com.ashwin.weatherapi.weatherapi.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.Deque;
import java.util.LinkedList;

@Component
public class RateLimitingFilter implements Filter {

    @Value("${rate.limit.requests}")
    private int maxRequests;

    @Value("${rate.limit.window.seconds}")
    private int windowSeconds;

    private final ConcurrentMap<String, Deque<Long>> requestCounts = new ConcurrentHashMap<>();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String clientIp = getClientIp(httpRequest);

        long currentTime = System.currentTimeMillis();
        long windowStart = currentTime - (windowSeconds * 1000L);

        requestCounts.compute(clientIp, (key, timestamps) -> {
            if (timestamps == null) {
                timestamps = new LinkedList<>();
            }
            // Remove old timestamps outside the window
            timestamps.removeIf(timestamp -> timestamp < windowStart);
            // Add current timestamp
            timestamps.add(currentTime);
            return timestamps;
        });

        Deque<Long> timestamps = requestCounts.get(clientIp);
        if (timestamps.size() > maxRequests) {
            httpResponse.setStatus(429);
            httpResponse.getWriter().write("Too many requests");
            return;
        }

        chain.doFilter(request, response);
    }

    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        return request.getRemoteAddr();
    }
}
