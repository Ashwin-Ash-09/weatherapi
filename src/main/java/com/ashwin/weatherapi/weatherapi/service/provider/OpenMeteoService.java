package com.ashwin.weatherapi.weatherapi.service.provider;

import com.ashwin.weatherapi.weatherapi.model.CurrentWeather;
import com.ashwin.weatherapi.weatherapi.model.Forecast;
import com.ashwin.weatherapi.weatherapi.model.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.ArrayList;

@Service
public class OpenMeteoService {

    private static final Logger logger = LoggerFactory.getLogger(OpenMeteoService.class);
    @Autowired
    private RestTemplate restTemplate;

    private static final String API_URL = "https://api.open-meteo.com/v1/forecast?latitude={lat}&longitude={lon}&current=temperature_2m,relative_humidity_2m,windspeed_10m,weathercode";

    private static final String FORECAST_URL = "https://api.open-meteo.com/v1/forecast?latitude={lat}&longitude={lon}&daily=temperature_2m_max,temperature_2m_min,weathercode&forecast_days={days}";

    private String getWeatherDescription(int code) {
        return switch (code) {
            case 0 -> "Clear sky";
            case 1 -> "Mainly clear";
            case 2 -> "Partly cloudy";
            case 3 -> "Overcast";
            case 45, 48 -> "Fog";
            case 51, 53, 55 -> "Drizzle";
            case 61, 63, 65 -> "Rain";
            case 71, 73, 75 -> "Snow";
            case 80, 81, 82 -> "Showers";
            case 95, 96, 99 -> "Thunderstorm";
            default -> "Unknown (" + code + ")";
        };
    }

    public CurrentWeather getCurrentWeather(Location location) {
        try {
            var response = restTemplate.getForObject(API_URL, OpenMeteoResponse.class, location.getLat(), location.getLon());
            if (response != null && response.current != null) {
                var current = response.current;
                return new CurrentWeather(
                    location,
                    current.temperature_2m,
                    getWeatherDescription(current.weathercode),
                    current.relative_humidity_2m,
                    current.windspeed_10m
                );
            }
        } catch (Exception e) {
            logger.error("Error fetching current weather from Open-Meteo for {}: {}", location.getName(), e.getMessage());
        }
        return null;
    }

    public Forecast getForecast(Location location, int days) {
        try {
            var response = restTemplate.getForObject(FORECAST_URL, OpenMeteoForecastResponse.class, location.getLat(), location.getLon(), days);
            if (response != null && response.daily != null) {
                List<Forecast.DailyForecast> dailyForecasts = new ArrayList<>();
                String[] times = response.daily.time;
                double[] maxTemps = response.daily.temperature_2m_max;
                double[] minTemps = response.daily.temperature_2m_min;
                int[] codes = response.daily.weathercode;
                int forecastDays = Math.min(days, times.length);
                for (int i = 0; i < forecastDays; i++) {
                    String date = times[i];
                    double maxTemp = maxTemps[i];
                    double minTemp = minTemps[i];
                    String description = getWeatherDescription(codes[i]);
                    dailyForecasts.add(new Forecast.DailyForecast(date, maxTemp, minTemp, description));
                }
                return new Forecast(location, dailyForecasts);
            }
        } catch (Exception e) {
            logger.error("Error fetching forecast from Open-Meteo for {}: {}", location.getName(), e.getMessage());
        }
        return null;
    }

    public static class OpenMeteoResponse {
        public CurrentWeatherResponse current;
    }

    public static class CurrentWeatherResponse {
        public double temperature_2m;
        public int relative_humidity_2m;
        public double windspeed_10m;
        public int weathercode;
    }

    public static class OpenMeteoForecastResponse {
        public Daily daily;
    }

    public static class Daily {
        public String[] time;
        public double[] temperature_2m_max;
        public double[] temperature_2m_min;
        public int[] weathercode;
    }
}
