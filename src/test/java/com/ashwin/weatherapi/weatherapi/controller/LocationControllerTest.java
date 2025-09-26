package com.ashwin.weatherapi.weatherapi.controller;

import com.ashwin.weatherapi.weatherapi.model.Location;
import com.ashwin.weatherapi.weatherapi.service.LocationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LocationController.class)
class LocationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LocationService locationService;

    @Test
    void searchLocations_Success() throws Exception {
        Location location1 = new Location("London", 51.5074, -0.1278, "GB");
        Location location2 = new Location("London, Ontario", 42.9849, -81.2453, "CA");
        List<Location> locations = Arrays.asList(location1, location2);

        when(locationService.searchLocations("London")).thenReturn(locations);

        mockMvc.perform(get("/locations/search")
                .param("q", "London"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("London"))
                .andExpect(jsonPath("$[1].name").value("London, Ontario"));
    }

    @Test
    void searchLocations_NotFound() throws Exception {
        when(locationService.searchLocations("NonExistent")).thenReturn(List.of());

        mockMvc.perform(get("/locations/search")
                .param("q", "NonExistent"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.error").value("No locations found for: NonExistent"));
    }

    @Test
    void searchLocations_BlankQuery() throws Exception {
        mockMvc.perform(get("/locations/search")
                .param("q", ""))
                .andExpect(status().isBadRequest());
    }
}
