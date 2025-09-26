package com.ashwin.weatherapi.weatherapi.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Aggregated weather response including current weather and forecast")
public class WeatherResponse {
    @Schema(description = "Location details", implementation = Location.class)
    private Location location;
    @Schema(description = "Current weather data")
    private CurrentWeather currentWeather;
    @Schema(description = "Weather forecast data")
    private Forecast forecast;

    // Constructors
    public WeatherResponse() {}

    public WeatherResponse(Location location, CurrentWeather currentWeather, Forecast forecast) {
        this.location = location;
        this.currentWeather = currentWeather;
        this.forecast = forecast;
    }

    // Getters and Setters
    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }

    public CurrentWeather getCurrentWeather() { return currentWeather; }
    public void setCurrentWeather(CurrentWeather currentWeather) { this.currentWeather = currentWeather; }

    public Forecast getForecast() { return forecast; }
    public void setForecast(Forecast forecast) { this.forecast = forecast; }
}
