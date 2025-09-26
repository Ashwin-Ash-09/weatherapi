package com.ashwin.weatherapi.weatherapi.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Current weather data for a location")
public class CurrentWeather {
    @Schema(description = "Location details", implementation = Location.class)
    private Location location;
    @Schema(description = "Current temperature in Celsius", example = "15.5")
    private double temperature;
    @Schema(description = "Weather description", example = "Partly cloudy")
    private String description;
    @Schema(description = "Humidity percentage", example = "65")
    private int humidity;
    @Schema(description = "Wind speed in km/h", example = "10.2")
    private double windSpeed;

    // Constructors
    public CurrentWeather() {}

    public CurrentWeather(Location location, double temperature, String description, int humidity, double windSpeed) {
        this.location = location;
        this.temperature = temperature;
        this.description = description;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
    }

    // Getters and Setters
    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }

    public double getTemperature() { return temperature; }
    public void setTemperature(double temperature) { this.temperature = temperature; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getHumidity() { return humidity; }
    public void setHumidity(int humidity) { this.humidity = humidity; }

    public double getWindSpeed() { return windSpeed; }
    public void setWindSpeed(double windSpeed) { this.windSpeed = windSpeed; }
}
