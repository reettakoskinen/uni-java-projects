/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fi.tuni.prog3.weatherapp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the {@link DailyWeather} class.
 */
public class DailyWeatherTest {
    
    /**
     * Test the {@link DailyWeather#getDate()} method.
     */
    @Test
    public void testGetDate() {
        DailyWeather dailyWeather = new DailyWeather("2023-01-01", 30.5, 20.2, "Clear sky", 800);
        assertEquals("2023-01-01", dailyWeather.getDate());
    }
    /**
     * Test the {@link DailyWeather#getMaxTemp()} method.
     */
    @Test
    public void testGetMaxTemp() {
        DailyWeather dailyWeather = new DailyWeather("2023-01-01", 30.5, 20.2, "Clear sky", 800);
        assertEquals(30.5, dailyWeather.getMaxTemp(), 0.01); // Use delta for double comparison
    }
    /**
     * Test the {@link DailyWeather#getMinTemp()} method.
     */
    @Test
    public void testGetMinTemp() {
        DailyWeather dailyWeather = new DailyWeather("2023-01-01", 30.5, 20.2, "Clear sky", 800);
        assertEquals(20.2, dailyWeather.getMinTemp(), 0.01); // Use delta for double comparison
    }
    /**
     * Test the {@link DailyWeather#getDescription()} method.
     */
    @Test
    public void testGetDescription() {
        DailyWeather dailyWeather = new DailyWeather("2023-01-01", 30.5, 20.2, "Clear sky", 800);
        assertEquals("Clear sky", dailyWeather.getDescription());
    }
    /**
     * Test the {@link DailyWeather#getWeatherCode()} method.
     */
    @Test
    public void testGetWeatherCode() {
        DailyWeather dailyWeather = new DailyWeather("2023-01-01", 30.5, 20.2, "Clear sky", 800);
        assertEquals(800, dailyWeather.getWeatherCode());
    }

}
