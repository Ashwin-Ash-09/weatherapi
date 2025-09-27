package com.ashwin.weatherapi.weatherapi.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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

}
