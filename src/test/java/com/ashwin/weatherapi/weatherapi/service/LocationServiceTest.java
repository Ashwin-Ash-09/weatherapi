package com.ashwin.weatherapi.weatherapi.service;

import com.ashwin.weatherapi.weatherapi.exception.WeatherApiException;
import com.ashwin.weatherapi.weatherapi.model.Location;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LocationServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private LocationService locationService;

    @Test
    void testSearchLocations_Success() {
        LocationService.NominatimResponse[] mockResponses = new LocationService.NominatimResponse[1];
        mockResponses[0] = new LocationService.NominatimResponse();
        mockResponses[0].display_name = "Test City, Test Country";
        mockResponses[0].lat = "12.34";
        mockResponses[0].lon = "56.78";

        when(restTemplate.getForObject(anyString(), any(), anyString())).thenReturn(mockResponses);

        List<Location> locations = locationService.searchLocations("Test");

        assertFalse(locations.isEmpty());
        assertEquals(1, locations.size());
        Location location = locations.get(0);
        assertEquals("Test City", location.getName());
        assertEquals(12.34, location.getLat());
        assertEquals(56.78, location.getLon());
        assertEquals("Test Country", location.getCountry());
    }

    @Test
    void testSearchLocations_RestClientException() {
        when(restTemplate.getForObject(anyString(), any(), anyString())).thenThrow(new RestClientException("API Error"));

        List<Location> locations = locationService.searchLocations("Test");

        assertTrue(locations.isEmpty());
    }

    @Test
    void testSearchLocations_NumberFormatException() {
        LocationService.NominatimResponse[] mockResponses = new LocationService.NominatimResponse[1];
        mockResponses[0] = new LocationService.NominatimResponse();
        mockResponses[0].display_name = "Test City, Test Country";
        mockResponses[0].lat = "invalid";
        mockResponses[0].lon = "56.78";

        when(restTemplate.getForObject(anyString(), any(), anyString())).thenReturn(mockResponses);

        assertThrows(WeatherApiException.class, () -> locationService.searchLocations("Test"));
    }
}
