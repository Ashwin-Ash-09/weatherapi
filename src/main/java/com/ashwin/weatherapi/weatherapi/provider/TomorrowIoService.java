package com.ashwin.weatherapi.weatherapi.provider;

import com.ashwin.weatherapi.weatherapi.model.CurrentWeather;
import com.ashwin.weatherapi.weatherapi.model.Forecast;
import com.ashwin.weatherapi.weatherapi.model.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TomorrowIoService {

    private static final Logger logger = LoggerFactory.getLogger(TomorrowIoService.class);
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${tomorrow.io.api.key}")
    private String apiKey;

    private static final String API_URL = "https://api.tomorrow.io/v4/timelines?location={lat},{lon}&fields=temperature,humidity,windSpeed,weatherCode&timesteps=1d&units=metric&apikey={apiKey}";

    public CurrentWeather getCurrentWeather(Location location) {
        try {
            var response = restTemplate.getForObject(API_URL, TomorrowIoResponse.class, location.getLat(), location.getLon(), apiKey);
            if (response != null && response.data != null && response.data.timelines != null && !response.data.timelines.isEmpty()) {
                var timeline = response.data.timelines.get(0);
                if (timeline.intervals != null && !timeline.intervals.isEmpty()) {
                    var interval = timeline.intervals.get(0);
                    return new CurrentWeather(
                        location.getName(),
                        interval.values.temperature,
                        "", // Description not available
                        (int) interval.values.humidity,
                        interval.values.windSpeed
                    );
                }
            }
        } catch (Exception e) {
            logger.error("Error fetching current weather from Tomorrow.io for {}: {}", location.getName(), e.getMessage());
        }
        return null;
    }

    public Forecast getForecast(Location location, int days) {
        try {
            var response = restTemplate.getForObject(API_URL, TomorrowIoResponse.class, location.getLat(), location.getLon(), apiKey);
            if (response != null && response.data != null && response.data.timelines != null && !response.data.timelines.isEmpty()) {
                var timeline = response.data.timelines.get(0);
                if (timeline.intervals != null && !timeline.intervals.isEmpty()) {
                    List<Forecast.DailyForecast> dailyForecasts = new ArrayList<>();
                    for (int i = 0; i < Math.min(days, timeline.intervals.size()); i++) {
                        var interval = timeline.intervals.get(i);
                        dailyForecasts.add(new Forecast.DailyForecast(
                            LocalDate.parse(interval.startTime.substring(0, 10)).toString(),
                            interval.values.temperature, // This is the daily average
                            interval.values.temperature, // This is the daily average
                            "" // Description not available
                        ));
                    }
                    return new Forecast(location.getName(), dailyForecasts);
                }
            }
        } catch (Exception e) {
            logger.error("Error fetching forecast from Tomorrow.io for {}: {}", location.getName(), e.getMessage());
        }
        return null;
    }

    private static class TomorrowIoResponse {
        public Data data;
    }

    private static class Data {
        public List<Timeline> timelines;
    }

    private static class Timeline {
        public String timestep;
        public String startTime;
        public String endTime;
        public List<Interval> intervals;
    }

    private static class Interval {
        public String startTime;
        public Values values;
    }

    private static class Values {
        public double temperature;
        public double humidity;
        public double windSpeed;
        public int weatherCode;
    }
}