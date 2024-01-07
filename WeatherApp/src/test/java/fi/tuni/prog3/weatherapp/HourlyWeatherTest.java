package fi.tuni.prog3.weatherapp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the {@link HourlyWeather} class.
 */
public class HourlyWeatherTest {
    
    /**
     * Test the {@link HourlyWeather#getTime()} method.
     */
    @Test
    public void testGetTime() {
        HourlyWeather hourlyWeather = new HourlyWeather("12:00 PM", "25°C", 800);
        assertEquals("12:00 PM", hourlyWeather.getTime());
    }
    /**
     * Test the {@link HourlyWeather#getTemp()} method.
     */
    @Test
    public void testGetTemp() {
        HourlyWeather hourlyWeather = new HourlyWeather("12:00 PM", "25°C", 800);
        assertEquals("25°C", hourlyWeather.getTemp());
    }
    /**
     * Test the {@link HourlyWeather#getWeatherCode()} method.
     */
    @Test
    public void testGetWeatherCode() {
        HourlyWeather hourlyWeather = new HourlyWeather("12:00 PM", "25°C", 800);
        assertEquals(800, hourlyWeather.getWeatherCode());
    }

}
