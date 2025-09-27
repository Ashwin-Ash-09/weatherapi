# TODO: Implement IP-based Rate Limiting

- [x] Update pom.xml: Add spring-boot-starter-security dependency
- [x] Update application.properties: Add rate.limit.requests=10 and rate.limit.window.seconds=60
- [x] Create config/SecurityConfig.java: Enable WebSecurity and register RateLimitingFilter
- [x] Create config/RateLimitingFilter.java: Implement custom filter for IP rate limiting using ConcurrentHashMap
- [ ] Build the project: Run mvn clean install
- [ ] Run the application: mvn spring-boot:run
- [ ] Manual test: Make >10 requests to /weather/current from same IP within 60 seconds, check for 429 status
