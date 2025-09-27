package com.ashwin.weatherapi.weatherapi.provider;

import com.ashwin.weatherapi.weatherapi.model.CurrentWeather;
import com.ashwin.weatherapi.weatherapi.model.Forecast;
import com.ashwin.weatherapi.weatherapi.model.Location;
import com.ashwin.weatherapi.weatherapi.service.provider.WeatherApiComService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WeatherApiComServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private WeatherApiComService weatherApiComService;

    private Location location;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(weatherApiComService, "apiKey", "test-api-key");
        location = new Location();
        location.setName("Test City");
        location.setLat(12.34);
        location.setLon(56.78);
    }

    private WeatherApiComService.WeatherApiComResponse createMockResponse() {
        WeatherApiComService.WeatherApiComResponse response = new WeatherApiComService.WeatherApiComResponse();
        WeatherApiComService.Current current = new WeatherApiComService.Current();
        current.temp_c = 25.0;
        current.humidity = 80;
        current.wind_kph = 15.0;
        WeatherApiComService.Condition condition = new WeatherApiComService.Condition();
        condition.text = "Sunny";
        current.condition = condition;
        response.current = current;
        return response;
    }

    @Test
    void testGetCurrentWeather_Success() {
        WeatherApiComService.WeatherApiComResponse mockResponse = createMockResponse();
        when(restTemplate.getForObject(anyString(), any(), any(Object[].class))).thenReturn(mockResponse);

        CurrentWeather currentWeather = weatherApiComService.getCurrentWeather(location);

        assertNotNull(currentWeather);
        assertEquals(25.0, currentWeather.getTemperature());
        assertEquals("Sunny", currentWeather.getDescription());
        assertEquals(80, currentWeather.getHumidity());
        assertEquals(15.0, currentWeather.getWindSpeed());
    }

    @Test
    void testGetCurrentWeather_ApiError() {
        when(restTemplate.getForObject(anyString(), any(), any(Object[].class))).thenThrow(new RuntimeException("API Error"));

        CurrentWeather currentWeather = weatherApiComService.getCurrentWeather(location);

        assertNull(currentWeather);
    }

    @Test
    void testGetForecast() {
        Forecast forecast = weatherApiComService.getForecast(location, 5);
        assertNull(forecast);
    }
}
