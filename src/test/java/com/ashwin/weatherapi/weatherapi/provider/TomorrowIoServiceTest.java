package com.ashwin.weatherapi.weatherapi.provider;

import com.ashwin.weatherapi.weatherapi.model.CurrentWeather;
import com.ashwin.weatherapi.weatherapi.model.Forecast;
import com.ashwin.weatherapi.weatherapi.model.Location;
import com.ashwin.weatherapi.weatherapi.service.provider.TomorrowIoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TomorrowIoServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private TomorrowIoService tomorrowIoService;

    private Location location;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(tomorrowIoService, "apiKey", "test-api-key");
        location = new Location();
        location.setName("Test City");
        location.setLat(12.34);
        location.setLon(56.78);
    }

    private TomorrowIoService.TomorrowIoResponse createMockResponse() {
        TomorrowIoService.TomorrowIoResponse response = new TomorrowIoService.TomorrowIoResponse();
        TomorrowIoService.Data data = new TomorrowIoService.Data();
        TomorrowIoService.Timeline timeline = new TomorrowIoService.Timeline();
        TomorrowIoService.Interval interval = new TomorrowIoService.Interval();
        TomorrowIoService.Values values = new TomorrowIoService.Values();

        values.temperature = 25.0;
        values.humidity = 60.0;
        values.windSpeed = 10.0;
        values.weatherCode = 1000;

        interval.startTime = LocalDate.now().toString() + "T00:00:00Z";
        interval.values = values;

        timeline.intervals = Collections.singletonList(interval);
        data.timelines = Collections.singletonList(timeline);
        response.data = data;

        return response;
    }

    @Test
    void testGetCurrentWeather_Success() {
        TomorrowIoService.TomorrowIoResponse mockResponse = createMockResponse();
        when(restTemplate.getForObject(anyString(), any(), any(Object[].class))).thenReturn(mockResponse);

        CurrentWeather currentWeather = tomorrowIoService.getCurrentWeather(location);

        assertNotNull(currentWeather);
        assertEquals(25.0, currentWeather.getTemperature());
        assertEquals(60, currentWeather.getHumidity());
        assertEquals(10.0, currentWeather.getWindSpeed());
        assertEquals("Test City", currentWeather.getLocation().getName());
    }

    @Test
    void testGetCurrentWeather_ApiError() {
        when(restTemplate.getForObject(anyString(), any(), any(Object[].class))).thenThrow(new RuntimeException("API Error"));

        CurrentWeather currentWeather = tomorrowIoService.getCurrentWeather(location);

        assertNull(currentWeather);
    }
    
    @Test
    void testGetForecast_Success() {
        TomorrowIoService.TomorrowIoResponse mockResponse = createMockResponse();
        when(restTemplate.getForObject(anyString(), any(), any(Object[].class))).thenReturn(mockResponse);

        Forecast forecast = tomorrowIoService.getForecast(location, 1);

        assertNotNull(forecast);
        assertEquals(1, forecast.getDailyForecasts().size());
        Forecast.DailyForecast dailyForecast = forecast.getDailyForecasts().get(0);
        assertEquals(LocalDate.now().toString(), dailyForecast.getDate());
        assertEquals(25.0, dailyForecast.getMaxTemp());
        assertEquals(25.0, dailyForecast.getMinTemp());
    }

    @Test
    void testGetForecast_ApiError() {
        when(restTemplate.getForObject(anyString(), any(), any(Object[].class))).thenThrow(new RuntimeException("API Error"));

        Forecast forecast = tomorrowIoService.getForecast(location, 5);

        assertNull(forecast);
    }
}