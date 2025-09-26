package com.ashwin.weatherapi.weatherapi.provider;

import com.ashwin.weatherapi.weatherapi.model.CurrentWeather;
import com.ashwin.weatherapi.weatherapi.model.Forecast;
import com.ashwin.weatherapi.weatherapi.model.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class SevenTimerService {

    private static final Logger logger = LoggerFactory.getLogger(SevenTimerService.class);
    private final RestTemplate restTemplate = new RestTemplate();

    private static final String API_URL = "http://www.7timer.info/bin/api.pl?lon={lon}&lat={lat}&product=civil&output=json";

    public CurrentWeather getCurrentWeather(Location location) {
        try {
            var response = restTemplate.getForObject(API_URL, SevenTimerResponse.class, location.getLon(), location.getLat());
            if (response != null && response.dataseries != null && !response.dataseries.isEmpty()) {
                var current = response.dataseries.get(0);
                return new CurrentWeather(
                        location.getName(),
                        current.temp2m,
                        current.weather,
                        Integer.parseInt(current.rh2m.replace("%", "")),
                        current.wind10m.speed
                );
            }
        } catch (Exception e) {
            logger.error("Error fetching current weather from 7Timer for {}: {}", location.getName(), e.getMessage());
        }
        return null;
    }

    public Forecast getForecast(Location location, int days) {
        // Not implemented yet
        return null;
    }

    private static class SevenTimerResponse {
        public List<DataSeries> dataseries;
    }

    private static class DataSeries {
        public int temp2m;
        public String rh2m;
        public Wind10m wind10m;
        public String weather;
    }

    private static class Wind10m {
        public String direction;
        public int speed;
    }
}
