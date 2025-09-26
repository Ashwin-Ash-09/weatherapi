package com.ashwin.weatherapi.weatherapi.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Weather forecast data for a location")
public class Forecast {
    @Schema(description = "Location details", implementation = Location.class)
    private Location location;
    @Schema(description = "List of daily forecasts")
    private List<DailyForecast> dailyForecasts;

    // Constructors
    public Forecast() {}

    public Forecast(Location location, List<DailyForecast> dailyForecasts) {
        this.location = location;
        this.dailyForecasts = dailyForecasts;
    }

    // Getters and Setters
    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }

    public List<DailyForecast> getDailyForecasts() { return dailyForecasts; }
    public void setDailyForecasts(List<DailyForecast> dailyForecasts) { this.dailyForecasts = dailyForecasts; }

    @Schema(description = "Daily forecast details")
    public static class DailyForecast {
        @Schema(description = "Date of the forecast", example = "2023-10-01")
        private String date;
        @Schema(description = "Maximum temperature in Celsius", example = "20.5")
        private double maxTemp;
        @Schema(description = "Minimum temperature in Celsius", example = "10.2")
        private double minTemp;
        @Schema(description = "Weather description", example = "Sunny")
        private String description;

        public DailyForecast() {}

        public DailyForecast(String date, double maxTemp, double minTemp, String description) {
            this.date = date;
            this.maxTemp = maxTemp;
            this.minTemp = minTemp;
            this.description = description;
        }

        // Getters and Setters
        public String getDate() { return date; }
        public void setDate(String date) { this.date = date; }

        public double getMaxTemp() { return maxTemp; }
        public void setMaxTemp(double maxTemp) { this.maxTemp = maxTemp; }

        public double getMinTemp() { return minTemp; }
        public void setMinTemp(double minTemp) { this.minTemp = minTemp; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }
}
