package fi.tuni.prog3.weatherapp;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the non FX methods of WeatherApp class.
 */
public class WeatherAppTest {

    private Path tempFile;  // Temporary file to use for testing

    @BeforeEach
    public void setUp() throws IOException {
        // Create a temporary file for testing
        tempFile = Files.createTempFile("lastSearchedCityTest", ".txt");
    }

    @AfterEach
    public void tearDown() throws IOException {
        // Delete the temporary file after each test
        Files.deleteIfExists(tempFile);
    }

    /**
     * Test saving and loading the last searched city.
     */
    @Test
    public void testSaveAndLoadLastSearchedCity() {
        WeatherApp weatherApp = new WeatherApp();  // Pass the temporary file path to the WeatherApp constructor

        // Test saving and loading the last searched city
        String cityName = "Helsinki";
        weatherApp.saveLastSearchedCity(cityName);
        String loadedCity = weatherApp.loadLastSearchedCity();

        // Check if the loaded city matches the saved city
        assertEquals(cityName, loadedCity, "Saved and loaded city should match");
    }
}

