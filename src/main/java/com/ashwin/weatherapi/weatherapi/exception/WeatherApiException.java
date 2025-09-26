package com.ashwin.weatherapi.weatherapi.exception;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Exception thrown when external weather API calls fail")
public class WeatherApiException extends RuntimeException {
    public WeatherApiException(String message) {
        super(message);
    }

    public WeatherApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
