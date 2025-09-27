package com.ashwin.weatherapi.weatherapi.service;

import com.ashwin.weatherapi.weatherapi.model.CurrentWeather;
import com.ashwin.weatherapi.weatherapi.model.Forecast;
import com.ashwin.weatherapi.weatherapi.model.WeatherResponse;
import com.ashwin.weatherapi.weatherapi.provider.OpenMeteoService;
import com.ashwin.weatherapi.weatherapi.provider.SevenTimerService;
import com.ashwin.weatherapi.weatherapi.provider.TomorrowIoService;
import com.ashwin.weatherapi.weatherapi.provider.WeatherApiComService;
import com.ashwin.weatherapi.weatherapi.provider.WttrInService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class WeatherService {

    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);

    private final TomorrowIoService tomorrowIoService;
    private final WeatherApiComService weatherApiComService;
    private final OpenMeteoService openMeteoService;
    private final WttrInService wttrInService;
    private final SevenTimerService sevenTimerService;

    public WeatherService(TomorrowIoService tomorrowIoService, WeatherApiComService weatherApiComService, OpenMeteoService openMeteoService, WttrInService wttrInService, SevenTimerService sevenTimerService) {
        this.tomorrowIoService = tomorrowIoService;
        this.weatherApiComService = weatherApiComService;
        this.openMeteoService = openMeteoService;
        this.wttrInService = wttrInService;
        this.sevenTimerService = sevenTimerService;
    }

    @Cacheable(value = "currentWeather", key = "#location.name")
    public CurrentWeather getCurrentWeather(com.ashwin.weatherapi.weatherapi.model.Location location) {
        logger.info("Fetching current weather from all sources for location: {}", location.getName());
        List<CurrentWeather> results = new ArrayList<>();
        CurrentWeather result;

        result = tomorrowIoService.getCurrentWeather(location);
        if (result != null) results.add(result);

        result = weatherApiComService.getCurrentWeather(location);
        if (result != null) results.add(result);

        result = openMeteoService.getCurrentWeather(location);
        if (result != null) results.add(result);

        result = wttrInService.getCurrentWeather(location);
        if (result != null) results.add(result);

        result = sevenTimerService.getCurrentWeather(location);
        if (result != null) results.add(result);

        if (results.isEmpty()) {
            logger.warn("No current weather data available from any source for location: {}", location.getName());
            return null;
        }

        return aggregateCurrentWeather(results, location);
    }

    private CurrentWeather aggregateCurrentWeather(List<CurrentWeather> results, com.ashwin.weatherapi.weatherapi.model.Location location) {
        if (results.isEmpty()) return null;

        double totalTemp = 0;
        int totalHumidity = 0;
        double totalWindSpeed = 0;
        String description = "";

        for (CurrentWeather cw : results) {
            totalTemp += cw.getTemperature();
            totalHumidity += cw.getHumidity();
            totalWindSpeed += cw.getWindSpeed();
            if (description.isEmpty() && !cw.getDescription().isEmpty()) {
                description = cw.getDescription();
            }
        }

        int count = results.size();
        return new CurrentWeather(
            location,
            Math.round((totalTemp / count) * 100.0) / 100.0,
            description,
            totalHumidity / count,
            Math.round((totalWindSpeed / count) * 100.0) / 100.0
        );
    }

    private Forecast aggregateForecasts(List<Forecast> forecasts, com.ashwin.weatherapi.weatherapi.model.Location location, int days) {
        if (forecasts.isEmpty()) return null;

        Map<String, List<Forecast.DailyForecast>> dateMap = new TreeMap<>();
        for (Forecast f : forecasts) {
            if (f != null && f.getDailyForecasts() != null) {
                for (Forecast.DailyForecast df : f.getDailyForecasts()) {
                    dateMap.computeIfAbsent(df.getDate(), k -> new ArrayList<>()).add(df);
                }
            }
        }

        List<Forecast.DailyForecast> aggregated = new ArrayList<>();
        for (String date : dateMap.keySet()) {
            if (aggregated.size() >= days) break;
            List<Forecast.DailyForecast> dfs = dateMap.get(date);
            double totalMax = 0, totalMin = 0;
            String desc = "";
            for (Forecast.DailyForecast df : dfs) {
                totalMax += df.getMaxTemp();
                totalMin += df.getMinTemp();
                if (desc.isEmpty() && df.getDescription() != null && !df.getDescription().isEmpty()) {
                    desc = df.getDescription();
                }
            }
            int count = dfs.size();
            aggregated.add(new Forecast.DailyForecast(date, Math.round((totalMax / count) * 100.0) / 100.0, Math.round((totalMin / count) * 100.0) / 100.0, desc));
        }

        return new Forecast(location, aggregated);
    }

    @Cacheable(value = "forecast", key = "#location.name + '_' + #days")
    public Forecast getForecast(com.ashwin.weatherapi.weatherapi.model.Location location, int days) {
        logger.info("Fetching forecast from all sources for location: {}, days: {}", location.getName(), days);
        List<Forecast> results = new ArrayList<>();
        Forecast forecast;

        forecast = openMeteoService.getForecast(location, days);
        if (forecast != null) results.add(forecast);

        forecast = tomorrowIoService.getForecast(location, days);
        if (forecast != null) results.add(forecast);

        if (results.isEmpty()) {
            logger.warn("No forecast data available from any source for location: {}", location.getName());
            return null;
        }

        return aggregateForecasts(results, location, days);
    }

    public WeatherResponse getFullWeather(com.ashwin.weatherapi.weatherapi.model.Location location, int days) {
        logger.info("Fetching full weather (current + forecast) for location: {}, days: {}", location.getName(), days);
        CurrentWeather current = getCurrentWeather(location);
        Forecast forecast = getForecast(location, days);
        return new WeatherResponse(location, current, forecast);
    }
}
