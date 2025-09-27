package com.ashwin.weatherapi.weatherapi.provider;

import com.ashwin.weatherapi.weatherapi.model.CurrentWeather;
import com.ashwin.weatherapi.weatherapi.model.Forecast;
import com.ashwin.weatherapi.weatherapi.model.Location;
import com.ashwin.weatherapi.weatherapi.service.provider.WttrInService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WttrInServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private WttrInService wttrInService;

    private Location location;

    @BeforeEach
    void setUp() {
        location = new Location();
        location.setName("Test City");
        location.setLat(12.34);
        location.setLon(56.78);
    }

    private WttrInService.WttrInResponse createMockResponse() {
        WttrInService.WttrInResponse response = new WttrInService.WttrInResponse();
        WttrInService.CurrentCondition currentCondition = new WttrInService.CurrentCondition();
        currentCondition.temp_C = "25";
        currentCondition.humidity = "80";
        currentCondition.windspeedKmph = "15";
        WttrInService.WeatherDesc weatherDesc = new WttrInService.WeatherDesc();
        weatherDesc.value = "Sunny";
        currentCondition.weatherDesc = Collections.singletonList(weatherDesc);
        response.current_condition = Collections.singletonList(currentCondition);
        return response;
    }

    @Test
    void testGetCurrentWeather_Success() {
        WttrInService.WttrInResponse mockResponse = createMockResponse();
        when(restTemplate.getForObject(anyString(), any(), anyString())).thenReturn(mockResponse);

        CurrentWeather currentWeather = wttrInService.getCurrentWeather(location);

        assertNotNull(currentWeather);
        assertEquals(25.0, currentWeather.getTemperature());
        assertEquals("Sunny", currentWeather.getDescription());
        assertEquals(80, currentWeather.getHumidity());
        assertEquals(15.0, currentWeather.getWindSpeed());
    }

    @Test
    void testGetCurrentWeather_ApiError() {
        when(restTemplate.getForObject(anyString(), any(), anyString())).thenThrow(new RuntimeException("API Error"));

        CurrentWeather currentWeather = wttrInService.getCurrentWeather(location);

        assertNull(currentWeather);
    }

    @Test
    void testGetForecast() {
        Forecast forecast = wttrInService.getForecast(location, 5);
        assertNull(forecast);
    }
}
