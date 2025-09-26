package com.ashwin.weatherapi.weatherapi.provider;

import com.ashwin.weatherapi.weatherapi.model.CurrentWeather;
import com.ashwin.weatherapi.weatherapi.model.Forecast;
import com.ashwin.weatherapi.weatherapi.model.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class WttrInService {

    private static final Logger logger = LoggerFactory.getLogger(WttrInService.class);
    @Autowired
    private RestTemplate restTemplate;

    private static final String API_URL = "http://wttr.in/{location}?format=j1";

    public CurrentWeather getCurrentWeather(Location location) {
        try {
            var response = restTemplate.getForObject(API_URL, WttrInResponse.class, location.getName());
            if (response != null && response.current_condition != null && !response.current_condition.isEmpty()) {
                var current = response.current_condition.get(0);
                return new CurrentWeather(
                        location,
                        Double.parseDouble(current.temp_C),
                        current.weatherDesc.get(0).value,
                        Integer.parseInt(current.humidity),
                        Double.parseDouble(current.windspeedKmph)
                );
            }
        } catch (Exception e) {
            logger.error("Error fetching current weather from wttr.in for {}: {}", location.getName(), e.getMessage());
        }
        return null;
    }

    public Forecast getForecast(Location location, int days) {
        // Not implemented yet
        return null;
    }

    public static class WttrInResponse {
        public List<CurrentCondition> current_condition;
    }

    public static class CurrentCondition {
        public String temp_C;
        public String humidity;
        public String windspeedKmph;
        public List<WeatherDesc> weatherDesc;
    }

    public static class WeatherDesc {
        public String value;
    }
}
