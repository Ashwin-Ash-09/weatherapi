package com.ashwin.weatherapi.weatherapi.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Represents a geographical location")
public class Location {
    @Schema(description = "Name of the location (e.g., city name)", example = "London")
    private String name;
    @Schema(description = "Latitude coordinate", example = "51.5074")
    private double lat;
    @Schema(description = "Longitude coordinate", example = "-0.1278")
    private double lon;
    @Schema(description = "Country code or name", example = "GB")
    private String country;

    // Constructors
    public Location() {}

    public Location(String name, double lat, double lon, String country) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.country = country;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getLat() { return lat; }
    public void setLat(double lat) { this.lat = lat; }

    public double getLon() { return lon; }
    public void setLon(double lon) { this.lon = lon; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
}
