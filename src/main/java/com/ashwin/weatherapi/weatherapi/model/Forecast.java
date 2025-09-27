package com.ashwin.weatherapi.weatherapi.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Weather forecast data for a location")
public class Forecast {
    @Schema(description = "Location details", implementation = Location.class)
    private Location location;
    @Schema(description = "List of daily forecasts")
    private List<DailyForecast> dailyForecasts;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
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

    }
}
