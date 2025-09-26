# TODO: Fix Failing Tests

## 1. Update WeatherControllerTest
- Change test methods to use 'location' parameter instead of 'lat' and 'lon'
- Add @MockBean for LocationService
- Mock LocationService.searchLocations to return a list with the location

## 2. Modify LocationService
- Change RestTemplate to be injected via constructor
- Update constructor to accept RestTemplate

## 3. Update WeatherServiceTest
- Remove unnecessary stubbing for weatherApiComService.getForecast in getForecast_AllFail test

## 4. Add RestTemplate Bean
- Add @Bean RestTemplate in CacheConfig.java
