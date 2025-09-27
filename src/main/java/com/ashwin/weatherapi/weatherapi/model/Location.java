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

}
