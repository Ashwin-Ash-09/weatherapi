package com.ashwin.weatherapi.weatherapi.provider;

import com.ashwin.weatherapi.weatherapi.model.CurrentWeather;
import com.ashwin.weatherapi.weatherapi.model.Forecast;
import com.ashwin.weatherapi.weatherapi.model.Location;
import com.ashwin.weatherapi.weatherapi.service.provider.OpenMeteoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OpenMeteoServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private OpenMeteoService openMeteoService;

    private Location location;

    @BeforeEach
    void setUp() {
        location = new Location();
        location.setName("Test City");
        location.setLat(12.34);
        location.setLon(56.78);
    }

    private OpenMeteoService.OpenMeteoResponse createMockCurrentWeatherResponse() {
        OpenMeteoService.OpenMeteoResponse response = new OpenMeteoService.OpenMeteoResponse();
        OpenMeteoService.CurrentWeatherResponse current = new OpenMeteoService.CurrentWeatherResponse();
        current.temperature_2m = 25.0;
        current.relative_humidity_2m = 60;
        current.windspeed_10m = 10.0;
        current.weathercode = 0;
        response.current = current;
        return response;
    }

    private OpenMeteoService.OpenMeteoForecastResponse createMockForecastResponse() {
        OpenMeteoService.OpenMeteoForecastResponse response = new OpenMeteoService.OpenMeteoForecastResponse();
        OpenMeteoService.Daily daily = new OpenMeteoService.Daily();
        daily.time = new String[]{"2025-09-26"};
        daily.temperature_2m_max = new double[]{30.0};
        daily.temperature_2m_min = new double[]{20.0};
        daily.weathercode = new int[]{1};
        response.daily = daily;
        return response;
    }

    @Test
    void testGetCurrentWeather_Success() {
        OpenMeteoService.OpenMeteoResponse mockResponse = createMockCurrentWeatherResponse();
        when(restTemplate.getForObject(anyString(), any(), any(Object[].class))).thenReturn(mockResponse);

        CurrentWeather currentWeather = openMeteoService.getCurrentWeather(location);

        assertNotNull(currentWeather);
        assertEquals(25.0, currentWeather.getTemperature());
        assertEquals("Clear sky", currentWeather.getDescription());
        assertEquals(60, currentWeather.getHumidity());
        assertEquals(10.0, currentWeather.getWindSpeed());
    }

    @Test
    void testGetCurrentWeather_ApiError() {
        when(restTemplate.getForObject(anyString(), any(), any(Object[].class))).thenThrow(new RuntimeException("API Error"));

        CurrentWeather currentWeather = openMeteoService.getCurrentWeather(location);

        assertNull(currentWeather);
    }

    @Test
    void testGetForecast_Success() {
        OpenMeteoService.OpenMeteoForecastResponse mockResponse = createMockForecastResponse();
        when(restTemplate.getForObject(anyString(), any(), any(Object[].class))).thenReturn(mockResponse);

        Forecast forecast = openMeteoService.getForecast(location, 1);

        assertNotNull(forecast);
        assertEquals(1, forecast.getDailyForecasts().size());
        Forecast.DailyForecast dailyForecast = forecast.getDailyForecasts().get(0);
        assertEquals("2025-09-26", dailyForecast.getDate());
        assertEquals(30.0, dailyForecast.getMaxTemp());
        assertEquals(20.0, dailyForecast.getMinTemp());
        assertEquals("Mainly clear", dailyForecast.getDescription());
    }

    @Test
    void testGetForecast_ApiError() {
        when(restTemplate.getForObject(anyString(), any(), any(Object[].class))).thenThrow(new RuntimeException("API Error"));

        Forecast forecast = openMeteoService.getForecast(location, 1);

        assertNull(forecast);
    }
}
