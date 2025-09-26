package com.ashwin.weatherapi.weatherapi.service;

import com.ashwin.weatherapi.weatherapi.model.CurrentWeather;
import com.ashwin.weatherapi.weatherapi.model.Forecast;
import com.ashwin.weatherapi.weatherapi.model.Location;
import com.ashwin.weatherapi.weatherapi.provider.OpenMeteoService;
import com.ashwin.weatherapi.weatherapi.provider.SevenTimerService;
import com.ashwin.weatherapi.weatherapi.provider.TomorrowIoService;
import com.ashwin.weatherapi.weatherapi.provider.WeatherApiComService;
import com.ashwin.weatherapi.weatherapi.provider.WttrInService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WeatherServiceTest {

    @Mock
    private OpenMeteoService openMeteoService;

    @Mock
    private SevenTimerService sevenTimerService;

    @Mock
    private TomorrowIoService tomorrowIoService;

    @Mock
    private WeatherApiComService weatherApiComService;

    @Mock
    private WttrInService wttrInService;

    @InjectMocks
    private WeatherService weatherService;

    private Location location = new Location("Test City", 12.34, 56.78, "Test Country");

    @Test
    void getCurrentWeather_Success() {
        CurrentWeather expected = new CurrentWeather(location, 25.0, "Sunny", 60, 5.0);
        when(openMeteoService.getCurrentWeather(any(Location.class))).thenReturn(expected);

        CurrentWeather result = weatherService.getCurrentWeather(location);

        assertNotNull(result);
        assertEquals(25.0, result.getTemperature());
    }

    @Test
    void getCurrentWeather_Fallback() {
        when(openMeteoService.getCurrentWeather(any(Location.class))).thenReturn(null);
        CurrentWeather expected = new CurrentWeather(location, 20.0, "Cloudy", 70, 3.0);
        when(sevenTimerService.getCurrentWeather(any(Location.class))).thenReturn(expected);

        CurrentWeather result = weatherService.getCurrentWeather(location);

        assertNotNull(result);
        assertEquals(20.0, result.getTemperature());
    }

    @Test
    void getCurrentWeather_AllFail() {
        when(openMeteoService.getCurrentWeather(any(Location.class))).thenReturn(null);
        when(sevenTimerService.getCurrentWeather(any(Location.class))).thenReturn(null);
        when(tomorrowIoService.getCurrentWeather(any(Location.class))).thenReturn(null);
        when(weatherApiComService.getCurrentWeather(any(Location.class))).thenReturn(null);
        when(wttrInService.getCurrentWeather(any(Location.class))).thenReturn(null);

        CurrentWeather result = weatherService.getCurrentWeather(location);

        assertNull(result);
    }

    @Test
    void getForecast_Success() {
        Forecast expected = new Forecast(location, List.of());
        when(openMeteoService.getForecast(any(Location.class), anyInt())).thenReturn(expected);

        Forecast result = weatherService.getForecast(location, 3);

        assertNotNull(result);
        assertEquals("Test City", result.getLocation().getName());
    }

    @Test
    void getForecast_Fallback() {
        when(openMeteoService.getForecast(any(Location.class), anyInt())).thenReturn(null);
        Forecast expected = new Forecast(location, List.of());
        when(tomorrowIoService.getForecast(any(Location.class), anyInt())).thenReturn(expected);

        Forecast result = weatherService.getForecast(location, 3);

        assertNotNull(result);
    }

    @Test
    void getForecast_AllFail() {
        when(openMeteoService.getForecast(any(Location.class), anyInt())).thenReturn(null);
        when(tomorrowIoService.getForecast(any(Location.class), anyInt())).thenReturn(null);

        Forecast result = weatherService.getForecast(location, 3);

        assertNull(result);
    }
}
