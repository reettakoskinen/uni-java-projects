
package fi.tuni.prog3.weatherapp;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ReadFileTest {

    @Test
    public void testWriteAndReadFromFile() {
        // Arrange
        ReadFile readFile = new ReadFile();
        String testData = "Test data for file write and read.";

        // Act
        readFile.setDataToWrite(testData);
        boolean writeSuccess = false;
        boolean readSuccess = false;

        try {
            writeSuccess = readFile.writeToFile("testFile.txt");
            readSuccess = readFile.readFromFile("weatherData");
        } catch (IOException e) {
        }

        // Assert
        assertTrue(writeSuccess, "File write operation should be successful");
        assertTrue(readSuccess, "File read operation should be successful");
    }

    @AfterAll
    public static void cleanup() {
        // Clean up the testFile.txt
        try {
            Files.deleteIfExists(Path.of("weatherData", "testFile.txt"));
        } catch (IOException e) {
        }
    }
}

