package com.ashwin.weatherapi.weatherapi.model;

public class WeatherResponse {
    private String location;
    private CurrentWeather currentWeather;
    private Forecast forecast;

    // Constructors
    public WeatherResponse() {}

    public WeatherResponse(String location, CurrentWeather currentWeather, Forecast forecast) {
        this.location = location;
        this.currentWeather = currentWeather;
        this.forecast = forecast;
    }

    // Getters and Setters
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public CurrentWeather getCurrentWeather() { return currentWeather; }
    public void setCurrentWeather(CurrentWeather currentWeather) { this.currentWeather = currentWeather; }

    public Forecast getForecast() { return forecast; }
    public void setForecast(Forecast forecast) { this.forecast = forecast; }
}
