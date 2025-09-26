package com.ashwin.weatherapi.weatherapi.controller;

import com.ashwin.weatherapi.weatherapi.model.Location;
import com.ashwin.weatherapi.weatherapi.service.LocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/locations")
@Validated
@Tag(name = "Locations", description = "Location search endpoints")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    /**
     * Searches for locations matching the query.
     *
     * @param q the search query (e.g., city name)
     * @return ResponseEntity with list of locations or 404 if none found
     */
    @GetMapping("/search")
    @Operation(summary = "Search locations", description = "Searches for locations matching the query. Returns 404 if no locations are found.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful search", content = @Content(schema = @Schema(implementation = List.class))),
        @ApiResponse(responseCode = "400", description = "Invalid query"),
        @ApiResponse(responseCode = "404", description = "No locations found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class, example = "{\"error\": \"No locations found for: London\"}"))),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> searchLocations(
            @Parameter(description = "Search query", example = "London")
            @RequestParam @NotBlank String q) {
        List<Location> locations = locationService.searchLocations(q);
        if (locations.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("error", "No locations found for: " + q));
        }
        return ResponseEntity.ok(locations);
    }
}
