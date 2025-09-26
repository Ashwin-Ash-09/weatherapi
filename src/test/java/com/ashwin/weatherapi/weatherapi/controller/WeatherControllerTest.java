package com.ashwin.weatherapi.weatherapi.controller;

import com.ashwin.weatherapi.weatherapi.model.CurrentWeather;
import com.ashwin.weatherapi.weatherapi.model.Forecast;
import com.ashwin.weatherapi.weatherapi.model.Location;
import com.ashwin.weatherapi.weatherapi.service.LocationService;
import com.ashwin.weatherapi.weatherapi.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WeatherController.class)
class WeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeatherService weatherService;

    @MockBean
    private LocationService locationService;

    @Test
    void getCurrentWeather_Success() throws Exception {
        Location location = new Location("London", 51.5074, -0.1278, "GB");
        CurrentWeather currentWeather = new CurrentWeather(location, 15.0, "Sunny", 60, 5.0);

        when(locationService.searchLocations(anyString())).thenReturn(List.of(location));
        when(weatherService.getCurrentWeather(any(Location.class))).thenReturn(currentWeather);

        mockMvc.perform(get("/weather/current")
                .param("location", "London"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.temperature").value(15.0))
                .andExpect(jsonPath("$.description").value("Sunny"));
    }

    @Test
    void getCurrentWeather_InvalidLocation() throws Exception {
        mockMvc.perform(get("/weather/current")
                .param("location", ""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getForecast_Success() throws Exception {
        Location location = new Location("London", 51.5074, -0.1278, "GB");
        Forecast forecast = new Forecast(location, List.of());

        when(locationService.searchLocations(anyString())).thenReturn(List.of(location));
        when(weatherService.getForecast(any(Location.class), anyInt())).thenReturn(forecast);

        mockMvc.perform(get("/weather/forecast")
                .param("location", "London")
                .param("days", "3"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }

    @Test
    void getForecast_InvalidDays() throws Exception {
        when(locationService.searchLocations(anyString())).thenReturn(List.of(new Location("London", 51.5074, -0.1278, "GB")));

        mockMvc.perform(get("/weather/forecast")
                .param("location", "London")
                .param("days", "0"))
                .andExpect(status().isBadRequest());
    }
}
