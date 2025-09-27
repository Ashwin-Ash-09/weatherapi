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
        try {
            var response = restTemplate.getForObject(API_URL, WttrInResponse.class, location.getName());
            if (response != null && response.weather != null && !response.weather.isEmpty()) {
                List<Forecast.DailyForecast> dailyForecasts = response.weather.stream()
                        .limit(days)
                        .map(day -> {
                            String description = day.hourly.stream()
                                    .filter(h -> "1200".equals(h.time)) // Noon forecast
                                    .findFirst()
                                    .map(h -> h.weatherDesc.get(0).value)
                                    .orElse(day.hourly.get(0).weatherDesc.get(0).value); // Fallback to first hour

                            return new Forecast.DailyForecast(
                                    day.date,
                                    Double.parseDouble(day.maxtempC),
                                    Double.parseDouble(day.mintempC),
                                    description
                            );
                        })
                        .collect(java.util.stream.Collectors.toList());

                return new Forecast(location, dailyForecasts);
            }
        } catch (Exception e) {
            logger.error("Error fetching forecast from wttr.in for {}: {}", location.getName(), e.getMessage());
        }
        return null;
    }

    public static class WttrInResponse {
        public List<CurrentCondition> current_condition;
        public List<Weather> weather;
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

    public static class Weather {
        public String date;
        public String maxtempC;
        public String mintempC;
        public List<Hourly> hourly;
    }

    public static class Hourly {
        public String time;
        public String tempC;
        public List<WeatherDesc> weatherDesc;
    }
}
