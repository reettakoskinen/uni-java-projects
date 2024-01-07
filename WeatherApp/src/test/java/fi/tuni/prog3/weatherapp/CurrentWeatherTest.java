
package fi.tuni.prog3.weatherapp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the {@link CurrentWeather} class.
 */
public class CurrentWeatherTest {
    
    /**
     * Tests the getter methods of the {@link CurrentWeather} class.
     */
    @Test
    public void testGetters() {
        // Arrange
        double currentTemp = 25.5;
        double feelsLike = 26.0;
        int clouds = 20;
        int humidity = 70;
        String cityName = "TestCity";
        double rain = 5.5;
        double wind = 3.0;
        String description = "Clear sky";
        int weatherCode = 800;
        String sunset = "18:30";
        String sunrise = "06:00";

        // Act
        CurrentWeather currentWeather = new CurrentWeather(currentTemp, feelsLike, clouds, humidity, cityName, rain, wind, description, weatherCode, sunset, sunrise);

        // Assert
        assertEquals(currentTemp, currentWeather.getCurrentTemp(), 0.01, "Current temperature should match");
        assertEquals(feelsLike, currentWeather.getFeelsLike(), 0.01, "Feels like temperature should match");
        assertEquals(clouds, currentWeather.getClouds(), "Cloud cover percentage should match");
        assertEquals(humidity, currentWeather.getHumidity(), "Humidity percentage should match");
        assertEquals(cityName.toUpperCase(), currentWeather.getCityName(), "City name should match in uppercase");
        assertEquals(rain, currentWeather.getRain(), 0.01, "Rain amount should match");
        assertEquals(wind, currentWeather.getWind(), 0.01, "Wind speed should match");
        assertEquals(description, currentWeather.getDescription(), "Weather description should match");
        assertEquals(weatherCode, currentWeather.getWeatherCode(), "Weather code should match");
        assertEquals(sunset, currentWeather.getSunset(), "Sunset time should match");
        assertEquals(sunrise, currentWeather.getSunrise(), "Sunrise time should match");
    }
}
