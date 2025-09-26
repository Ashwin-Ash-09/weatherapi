package com.ashwin.weatherapi.weatherapi.model;

import java.util.List;

public class Forecast {
    private String location;
    private List<DailyForecast> dailyForecasts;

    // Constructors
    public Forecast() {}

    public Forecast(String location, List<DailyForecast> dailyForecasts) {
        this.location = location;
        this.dailyForecasts = dailyForecasts;
    }

    // Getters and Setters
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public List<DailyForecast> getDailyForecasts() { return dailyForecasts; }
    public void setDailyForecasts(List<DailyForecast> dailyForecasts) { this.dailyForecasts = dailyForecasts; }

    public static class DailyForecast {
        private String date;
        private double maxTemp;
        private double minTemp;
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
