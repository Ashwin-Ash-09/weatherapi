package com.ashwin.weatherapi.weatherapi.provider;

import com.ashwin.weatherapi.weatherapi.model.CurrentWeather;
import com.ashwin.weatherapi.weatherapi.model.Forecast;
import com.ashwin.weatherapi.weatherapi.model.Location;
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
class SevenTimerServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private SevenTimerService sevenTimerService;

    private Location location;

    @BeforeEach
    void setUp() {
        location = new Location();
        location.setName("Test City");
        location.setLat(12.34);
        location.setLon(56.78);
    }

    private SevenTimerService.SevenTimerResponse createMockResponse() {
        SevenTimerService.SevenTimerResponse response = new SevenTimerService.SevenTimerResponse();
        SevenTimerService.DataSeries dataSeries = new SevenTimerService.DataSeries();
        dataSeries.temp2m = 25;
        dataSeries.rh2m = "80%";
        dataSeries.weather = "clear";
        SevenTimerService.Wind10m wind = new SevenTimerService.Wind10m();
        wind.speed = 4;
        dataSeries.wind10m = wind;
        response.dataseries = Collections.singletonList(dataSeries);
        return response;
    }

    @Test
    void testGetCurrentWeather_Success() {
        SevenTimerService.SevenTimerResponse mockResponse = createMockResponse();
        when(restTemplate.getForObject(anyString(), any(), any(Object[].class))).thenReturn(mockResponse);

        CurrentWeather currentWeather = sevenTimerService.getCurrentWeather(location);

        assertNotNull(currentWeather);
        assertEquals(25.0, currentWeather.getTemperature());
        assertEquals("clear", currentWeather.getDescription());
        assertEquals(80, currentWeather.getHumidity());
        assertEquals(4.0, currentWeather.getWindSpeed());
    }

    @Test
    void testGetCurrentWeather_ApiError() {
        when(restTemplate.getForObject(anyString(), any(), any(Object[].class))).thenThrow(new RuntimeException("API Error"));

        CurrentWeather currentWeather = sevenTimerService.getCurrentWeather(location);

        assertNull(currentWeather);
    }

    @Test
    void testGetForecast() {
        Forecast forecast = sevenTimerService.getForecast(location, 5);
        assertNull(forecast);
    }
}
