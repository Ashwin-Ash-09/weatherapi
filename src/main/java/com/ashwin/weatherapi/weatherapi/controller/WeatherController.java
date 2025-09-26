package com.ashwin.weatherapi.weatherapi.controller;

import com.ashwin.weatherapi.weatherapi.model.CurrentWeather;
import com.ashwin.weatherapi.weatherapi.model.Forecast;
import com.ashwin.weatherapi.weatherapi.model.Location;
import com.ashwin.weatherapi.weatherapi.model.WeatherResponse;
import com.ashwin.weatherapi.weatherapi.service.LocationService;
import com.ashwin.weatherapi.weatherapi.service.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/weather")
@Validated
@Tag(name = "Weather", description = "Weather data endpoints")
public class WeatherController {

    private final WeatherService weatherService;
    private final LocationService locationService;

    public WeatherController(WeatherService weatherService, LocationService locationService) {
        this.weatherService = weatherService;
        this.locationService = locationService;
    }

    /**
     * Retrieves current weather data for a given location.
     *
     * @param location the city name or location query
     * @return ResponseEntity with CurrentWeather or 404 with error message if not found or unavailable
     */
    @GetMapping("/current")
    @Operation(summary = "Get current weather", description = "Retrieves current weather data for a given location using the primary matching location.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of current weather", content = @Content(schema = @Schema(implementation = CurrentWeather.class))),
        @ApiResponse(responseCode = "400", description = "Invalid location parameter"),
        @ApiResponse(responseCode = "404", description = "Location not found or weather data unavailable", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class, example = "{\"error\": \"Location not found: London\"}"))),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> getCurrentWeather(
            @Parameter(description = "City name or location query", example = "London")
            @RequestParam @NotBlank String location) {
        List<Location> locations = locationService.searchLocations(location);
        if (locations.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Location not found: " + location));
        }
        Location primaryLocation = locations.get(0);
        CurrentWeather weather = weatherService.getCurrentWeather(primaryLocation);
        if (weather != null) {
            return ResponseEntity.ok(weather);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Weather data not available for: " + location));
    }

    /**
     * Retrieves weather forecast for a given location and number of days (capped at 7).
     *
     * @param location the city name or location query
     * @param days number of forecast days (1-7)
     * @return ResponseEntity with Forecast or 404 with error message if not found or unavailable
     */
    @GetMapping("/forecast")
    @Operation(summary = "Get weather forecast", description = "Retrieves weather forecast for a given location and specified number of days (limited to 1-7 days).")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of forecast", content = @Content(schema = @Schema(implementation = Forecast.class))),
        @ApiResponse(responseCode = "400", description = "Invalid parameters (e.g., days < 1 or > 7)"),
        @ApiResponse(responseCode = "404", description = "Location not found or forecast data unavailable", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class, example = "{\"error\": \"Location not found: London\"}"))),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> getForecast(
            @Parameter(description = "City name or location query", example = "London")
            @RequestParam @NotBlank String location,
            @Parameter(description = "Number of days (1-7)", example = "3")
            @RequestParam @Min(1) @Max(7) int days) {
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
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Forecast data not available for: " + location));
    }

    /**
     * Retrieves aggregated current weather and forecast for a given location.
     *
     * @param location the city name or location query
     * @param days number of forecast days (1-7, defaults to 1)
     * @return ResponseEntity with WeatherResponse or 404 with error message if not found or unavailable
     */
    @GetMapping
    @Operation(summary = "Get full weather data", description = "Retrieves aggregated current weather and forecast for a given location with optional forecast days (limited to 1-7, defaults to 1).")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of full weather data", content = @Content(schema = @Schema(implementation = WeatherResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid parameters (e.g., days < 1 or > 7)"),
        @ApiResponse(responseCode = "404", description = "Location not found or weather data unavailable", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class, example = "{\"error\": \"Location not found: London\"}"))),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> getFullWeather(
            @Parameter(description = "City name or location query", example = "London")
            @RequestParam @NotBlank String location,
            @Parameter(description = "Number of forecast days (1-7)", example = "3")
            @RequestParam(defaultValue = "1") @Min(1) @Max(7) int days) {
        List<Location> locations = locationService.searchLocations(location);
        if (locations.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Location not found: " + location));
        }
        Location primaryLocation = locations.get(0);
        if (days > 7) days = 7; // Limit to 7 days
        WeatherResponse weatherResponse = weatherService.getFullWeather(primaryLocation, days);
        if (weatherResponse != null && weatherResponse.getCurrentWeather() != null) {
            return ResponseEntity.ok(weatherResponse);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Full weather data not available for: " + location));
    }
}
