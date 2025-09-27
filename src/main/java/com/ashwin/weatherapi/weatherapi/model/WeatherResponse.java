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
@Schema(description = "Aggregated weather response including current weather and forecast")
public class WeatherResponse {
    @Schema(description = "Location details", implementation = Location.class)
    private Location location;
    @Schema(description = "Current weather data")
    private CurrentWeather currentWeather;
    @Schema(description = "Weather forecast data")
    private Forecast forecast;


}
