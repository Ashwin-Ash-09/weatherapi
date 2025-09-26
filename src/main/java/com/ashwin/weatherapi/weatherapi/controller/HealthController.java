package com.ashwin.weatherapi.weatherapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Tag(name = "Health", description = "Health check endpoints")
public class HealthController {

    /**
     * Health check endpoint.
     * Returns the health status of the service.
     *
     * @return ResponseEntity with status "UP"
     */
    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Returns the health status of the service")
    @ApiResponse(responseCode = "200", description = "Service is healthy", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class, example = "{\"status\": \"UP\"}")))
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP"));
    }
}
