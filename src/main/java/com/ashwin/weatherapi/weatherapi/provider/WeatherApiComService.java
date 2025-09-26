
package com.ashwin.weatherapi.weatherapi.provider;

import com.ashwin.weatherapi.weatherapi.model.CurrentWeather;
import com.ashwin.weatherapi.weatherapi.model.Forecast;
import com.ashwin.weatherapi.weatherapi.model.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class WeatherApiComService {

    private static final Logger logger = LoggerFactory.getLogger(WeatherApiComService.class);
    @Autowired
    private RestTemplate restTemplate;

    @Value("${weather.api.com.api.key}")
    private String apiKey;

    private static final String API_URL = "http://api.weatherapi.com/v1/forecast.json?key={apiKey}&q={latitude},{longitude}&days={days}";

    public CurrentWeather getCurrentWeather(Location location) {
        try {
            var response = restTemplate.getForObject(API_URL, WeatherApiComResponse.class, apiKey, location.getLat(), location.getLon(), 1);
            if (response != null && response.current != null) {
                return new CurrentWeather(
                    location,
                    response.current.temp_c,
                    response.current.condition.text,
                    response.current.humidity,
                    response.current.wind_kph
                );
            }
        } catch (Exception e) {
            logger.error("Error fetching current weather from WeatherAPI.com for {}: {}", location.getName(), e.getMessage());
        }
        return null;
    }

    public Forecast getForecast(Location location, int days) {
        return null; // Not implemented yet
    }

    public static class WeatherApiComResponse {
        public Current current;
        public ForecastResponse forecast;
    }

    public static class Current {
        public double temp_c;
        public int humidity;
        public double wind_kph;
        public Condition condition;
    }

    public static class Condition {
        public String text;
    }

    public static class ForecastResponse {
        public List<ForecastDay> forecastday;
    }

    public static class ForecastDay {
        public String date;
        public Day day;
    }

    public static class Day {
        public double maxtemp_c;
        public double mintemp_c;
        public Condition condition;
    }
}
