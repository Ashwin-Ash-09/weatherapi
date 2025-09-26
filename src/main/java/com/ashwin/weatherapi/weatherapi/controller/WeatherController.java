package com.ashwin.weatherapi.weatherapi.controller;

import com.ashwin.weatherapi.weatherapi.model.CurrentWeather;
import com.ashwin.weatherapi.weatherapi.model.Forecast;
import com.ashwin.weatherapi.weatherapi.model.Location;
import com.ashwin.weatherapi.weatherapi.model.WeatherResponse;
import com.ashwin.weatherapi.weatherapi.service.LocationService;
import com.ashwin.weatherapi.weatherapi.service.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/weather")
@Validated
public class WeatherController {

    private final WeatherService weatherService;
    private final LocationService locationService;

    public WeatherController(WeatherService weatherService, LocationService locationService) {
        this.weatherService = weatherService;
        this.locationService = locationService;
    }

    @GetMapping("/current")
    @Operation(summary = "Get current weather", description = "Retrieves current weather data for a given location")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval"),
        @ApiResponse(responseCode = "400", description = "Invalid location"),
        @ApiResponse(responseCode = "404", description = "Location not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<CurrentWeather> getCurrentWeather(
            @Parameter(description = "City name", example = "London")
            @RequestParam @NotBlank String location) {
        List<Location> locations = locationService.searchLocations(location);
        if (locations.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Location primaryLocation = locations.get(0);
        CurrentWeather weather = weatherService.getCurrentWeather(primaryLocation);
        if (weather != null) {
            return ResponseEntity.ok(weather);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/forecast")
    @Operation(summary = "Get weather forecast", description = "Retrieves weather forecast for a given location and number of days")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval"),
        @ApiResponse(responseCode = "400", description = "Invalid parameters"),
        @ApiResponse(responseCode = "404", description = "Location not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> getForecast(
            @Parameter(description = "City name", example = "London")
            @RequestParam @NotBlank String location,
            @Parameter(description = "Number of days (1-7)", example = "3")
            @RequestParam @Min(1) int days) {
        List<Location> locations = locationService.searchLocations(location);
        if (locations.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Location not found: " + location));
        }
        Location primaryLocation = locations.get(0);
        if (days > 7) days = 7; // Limit to 7 days
        Forecast forecast = weatherService.getForecast(primaryLocation, days);
        if (forecast != null) {
            return ResponseEntity.ok(forecast);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    @Operation(summary = "Get full weather data", description = "Retrieves aggregated current weather and forecast for a given location")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval"),
        @ApiResponse(responseCode = "400", description = "Invalid parameters"),
        @ApiResponse(responseCode = "404", description = "Location not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<WeatherResponse> getFullWeather(
            @Parameter(description = "City name", example = "London")
            @RequestParam @NotBlank String location,
            @Parameter(description = "Number of forecast days (1-7)", example = "3")
            @RequestParam(defaultValue = "1") @Min(1) int days) {
        List<Location> locations = locationService.searchLocations(location);
        if (locations.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Location primaryLocation = locations.get(0);
        if (days > 7) days = 7; // Limit to 7 days
        WeatherResponse weatherResponse = weatherService.getFullWeather(primaryLocation, days);
        if (weatherResponse != null && weatherResponse.getCurrentWeather() != null) {
            return ResponseEntity.ok(weatherResponse);
        }
        return ResponseEntity.notFound().build();
    }
}
