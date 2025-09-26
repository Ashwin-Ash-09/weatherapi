package com.ashwin.weatherapi.weatherapi.service;

import com.ashwin.weatherapi.weatherapi.exception.WeatherApiException;
import com.ashwin.weatherapi.weatherapi.model.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LocationService {

    private static final Logger logger = LoggerFactory.getLogger(LocationService.class);
    private final RestTemplate restTemplate;

    public LocationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private static final String NOMINATIM_SEARCH_URL = "https://nominatim.openstreetmap.org/search?q={query}&format=json&limit=5";

    @Cacheable(value = "locationSearch", key = "#query")
    public List<Location> searchLocations(String query) {
        logger.info("Searching locations for query: {}", query);
        try {
            var responses = restTemplate.getForObject(NOMINATIM_SEARCH_URL, NominatimResponse[].class, query);
            if (responses != null) {
                return Arrays.stream(responses)
                    .map(r -> {
                        try {
                            String[] parts = r.display_name.split(",");
                            String name = parts[0].trim();
                            String country = parts.length > 0 ? parts[parts.length - 1].trim() : "";
                            return new Location(name, Double.parseDouble(r.lat), Double.parseDouble(r.lon), country);
                        } catch (NumberFormatException e) {
                            throw new WeatherApiException("Failed to parse location coordinates for " + r.display_name, e);
                        }
                    })
                    .collect(Collectors.toList());
            }
        } catch (RestClientException e) {
            logger.error("Error searching locations for {}: {}", query, e.getMessage());
        }
        return List.of();
    }

    public static class NominatimResponse {
        public String display_name;
        public String lat;
        public String lon;
    }
}
