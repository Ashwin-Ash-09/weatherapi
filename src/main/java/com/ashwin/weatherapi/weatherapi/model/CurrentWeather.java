package com.ashwin.weatherapi.weatherapi.model;

public class CurrentWeather {
    private String location;
    private double temperature;
    private String description;
    private int humidity;
    private double windSpeed;

    // Constructors
    public CurrentWeather() {}

    public CurrentWeather(String location, double temperature, String description, int humidity, double windSpeed) {
        this.location = location;
        this.temperature = temperature;
        this.description = description;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
    }

    // Getters and Setters
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public double getTemperature() { return temperature; }
    public void setTemperature(double temperature) { this.temperature = temperature; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getHumidity() { return humidity; }
    public void setHumidity(int humidity) { this.humidity = humidity; }

    public double getWindSpeed() { return windSpeed; }
    public void setWindSpeed(double windSpeed) { this.windSpeed = windSpeed; }
}
