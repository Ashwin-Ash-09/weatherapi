# TODO for Aggregating Weather Sources into Single Response

- [x] Create new model: src/main/java/com/ashwin/weatherapi/weatherapi/model/WeatherResponse.java (wrapper for CurrentWeather and Forecast)
- [x] Update WeatherService.java: Modify getCurrentWeather to fetch from all providers and aggregate successful results (average numerical fields, handle description)
- [x] Update WeatherController.java: Add unified GET /weather endpoint that returns WeatherResponse
- [x] Test: Run the application and verify the new endpoint aggregates data correctly

# TODO for Forecast Aggregation

- [x] Modify WeatherService.getForecast to fetch from OpenMeteo and TomorrowIo providers and aggregate forecasts
- [x] Add aggregateForecasts method to combine forecasts by averaging temperatures per date and selecting descriptions
- [x] Test the /weather/forecast and /weather endpoints to verify aggregation works
