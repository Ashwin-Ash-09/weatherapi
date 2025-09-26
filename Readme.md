# Weather API 🌤️

A comprehensive weather API that aggregates data from multiple weather service providers to give you the most accurate and reliable weather information.

## ✨ Features

- **Multi-Source Aggregation**: Combines data from 5+ weather providers for maximum accuracy
- **Current Weather**: Get real-time weather conditions for any location
- **Weather Forecast**: 7-day weather forecast with detailed daily information
- **Smart Caching**: Optimized performance with intelligent caching system
- **Location Search**: Flexible location search supporting city names
- **REST API**: Clean and intuitive REST endpoints
- **API Documentation**: Interactive Swagger UI for easy testing

## 🚀 Quick Start

### Prerequisites

- Java 17 or higher
- Maven 3.6+ or use the included Maven wrapper

### Installation & Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/Ashwin-Ash-09/weatherapi.git
   cd weatherapi
   ```

2. **Configure API Keys** (Optional)
   
   The application comes with demo API keys, but for production use, update the keys in `src/main/resources/application.properties`:
   ```properties
   tomorrow.io.api.key=YOUR_TOMORROW_IO_API_KEY
   weather.api.com.api.key=YOUR_WEATHER_API_COM_KEY
   ```

3. **Run the application**
   
   Using Maven wrapper (recommended):
   ```bash
   ./mvnw spring-boot:run
   ```
   
   Or with Maven:
   ```bash
   mvn spring-boot:run
   ```

4. **Access the API**
   - Base URL: `http://localhost:8080`
   - Swagger UI: `http://localhost:8080/swagger-ui.html`

## 📖 API Usage

### Get Current Weather
```bash
GET /weather/current?location=London
```

**Example Response:**
```json
{
  "location": {
    "name": "London",
    "lat": 51.5074,
    "lon": -0.1278,
    "country": "United Kingdom"
  },
  "temperature": 15.5,
  "description": "Partly cloudy",
  "humidity": 65,
  "windSpeed": 12.3
}
```

### Get Weather Forecast
```bash
GET /weather/forecast?location=London&days=5
```

**Example Response:**
```json
{
  "location": {
    "name": "London",
    "lat": 51.5074,
    "lon": -0.1278,
    "country": "United Kingdom"
  },
  "dailyForecasts": [
    {
      "date": "2024-01-15",
      "maxTemp": 18.0,
      "minTemp": 8.0,
      "description": "Sunny"
    }
  ]
}
```

### Get Complete Weather Data
```bash
GET /weather?location=London&days=3
```

**Example Response:**
```json
{
  "location": {
    "name": "London",
    "lat": 51.5074,
    "lon": -0.1278,
    "country": "United Kingdom"
  },
  "currentWeather": {
    "location": {
      "name": "London",
      "lat": 51.5074,
      "lon": -0.1278,
      "country": "United Kingdom"
    },
    "temperature": 15.5,
    "description": "Partly cloudy",
    "humidity": 65,
    "windSpeed": 12.3
  },
  "forecast": {
    "location": {
      "name": "London",
      "lat": 51.5074,
      "lon": -0.1278,
      "country": "United Kingdom"
    },
    "dailyForecasts": [...]
  }
}
```

## 🌍 Supported Locations

- **City Names**: "London", "New York", "Tokyo"
- **City, Country**: "Paris, France", "Sydney, Australia"
- **Coordinates**: Automatically resolved from city names

## 📊 Screenshots

### API Documentation (Swagger UI)
*Screenshot will be added here showing the interactive API documentation*

![API Documentation](screenshots/swagger-ui.png)

### Example API Response
*Screenshot will be added here showing a sample API response*

![API Response](screenshots/api-response.png)

### Application Logs
*Screenshot will be added here showing the application running*

![Application Logs](screenshots/app-logs.png)

## 🔧 Configuration

### Application Settings

The application can be configured through `application.properties`:

- **Port**: `server.port=8080`
- **Cache TTL**: `weather.cache.ttl=600` (10 minutes)
- **Logging Level**: `logging.level.com.ashwin.weatherapi=INFO`

### Environment Variables

You can also use environment variables:
```bash
export TOMORROW_IO_API_KEY=your_api_key
export WEATHER_API_COM_API_KEY=your_api_key
```

## ⚡ Performance Features

- **Intelligent Caching**: 10-minute cache TTL reduces API calls
- **Multi-Provider Fallback**: If one provider fails, others continue working
- **Data Aggregation**: Averages temperature, humidity, and wind speed from multiple sources
- **Request Validation**: Input validation for better error handling

## 🛠️ Development

### Build the Project
```bash
./mvnw clean package
```

### Run Tests
```bash
./mvnw test
```

### Generate Documentation
```bash
./mvnw spring-boot:run
# Visit http://localhost:8080/swagger-ui.html
```

## 📋 API Reference

| Endpoint | Method | Parameters | Description |
|----------|---------|------------|-------------|
| `/health` | GET | None | Health check |
| `/locations/search` | GET | `q` (string, required) | Search locations |
| `/weather/current` | GET | `location` (string, required) | Get current weather |
| `/weather/forecast` | GET | `location` (string, required), `days` (1-7, default=1) | Get weather forecast |
| `/weather` | GET | `location` (string, required), `days` (1-7, default=1) | Get complete weather data |

## ❗ Error Handling

The API returns appropriate HTTP status codes:

- **200**: Success
- **400**: Bad request (invalid parameters)
- **404**: Location not found
- **500**: Internal server error

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📝 License

This project is open source and available under the [MIT License](LICENSE).

## 🙋‍♂️ Support

If you have any questions or issues:

1. Check the [API Documentation](http://localhost:8080/swagger-ui.html)
2. Look at the [Technical Implementation Guide](TECHNICAL_IMPLEMENTATION.md)
3. Open an issue on GitHub

## 🏗️ Built With

- **Spring Boot 3.3.4** - Application framework
- **Java 17** - Programming language
- **Maven** - Dependency management
- **Caffeine** - Caching library
- **SpringDoc OpenAPI** - API documentation
- **Multiple Weather APIs** - Data sources

---

**Made with ❤️ by [Ashwin](https://github.com/Ashwin-Ash-09)**