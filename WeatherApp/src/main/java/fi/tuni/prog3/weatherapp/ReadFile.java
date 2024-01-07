package fi.tuni.prog3.weatherapp;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Implementation of the iReadAndWriteToFile interface for reading and writing weather data to files.
 */
public class ReadFile implements iReadAndWriteToFile {

    private String dataToWrite;
    private String currentJsonData;
    private String hourlyJsonData;
    private String dailyJsonData;

    /**
     * Sets the data to be written to a file.
     *
     * @param data The data to be written.
     */
    public void setDataToWrite(String data) {
        this.dataToWrite = data;
    }

    /**
     * Reads weather data from files in a specified folder.
     *
     * @param folderName The name of the folder containing weather data files.
     * @return True if reading is successful, false otherwise.
     * @throws IOException If an I/O error occurs.
     */
    @Override
    public boolean readFromFile(String folderName) throws IOException {
        Path folderPath = Paths.get(folderName);
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(folderPath)) {
            for (Path file : stream) {
                String fileContent = Files.readString(file);
                switch (file.getFileName().toString()) {
                    case "currentWeatherData":
                        currentJsonData = fileContent;
                        break;
                    case "hourlyWeatherData":
                        hourlyJsonData = fileContent;
                        break;
                    default:
                        dailyJsonData = fileContent;
                        break;
                }
            }
            return true;
        }
    }
    
    /**
     * Retrieves and parses the current weather data from JSON and returns it as a CurrentWeather object.
     *
     * @return A CurrentWeather object containing the parsed current weather data.
     */
    public CurrentWeather getCurrentWeather() {
        JsonObject jsonObject = JsonParser.parseString(currentJsonData).getAsJsonObject();
        double rain;
        // Saving the needed data
        double currentTemp = jsonObject.getAsJsonObject("main").get("temp").getAsDouble();
        double feelsLike = jsonObject.getAsJsonObject("main").get("feels_like").getAsDouble();
        int clouds = jsonObject.getAsJsonObject("clouds").get("all").getAsInt();
        int humidity = jsonObject.getAsJsonObject("main").get("humidity").getAsInt();
        String cityName = jsonObject.get("name").getAsString();
        try { 
            rain = jsonObject.getAsJsonObject("rain").get("1h").getAsDouble();
        } // Set rain as 0 if it is null
        catch (NullPointerException | NumberFormatException e) {
              rain = 0.0;
                }
        double wind = jsonObject.getAsJsonObject("wind").get("speed").getAsDouble();
        String description = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("description").getAsString();
        int weatherCode = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("id").getAsInt();
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH");
        long sunsetLong = jsonObject.getAsJsonObject("sys").get("sunset").getAsLong();
        LocalDateTime sunsetTime = convertTimestampToDate(sunsetLong);
        String sunset = sunsetTime.format(formatter);
        
        long sunriseLong = jsonObject.getAsJsonObject("sys").get("sunrise").getAsLong();
        LocalDateTime sunriseTime = convertTimestampToDate(sunriseLong);
        String sunrise = sunriseTime.format(formatter);

        return new CurrentWeather(currentTemp, feelsLike, clouds, humidity, cityName, rain , wind, description, weatherCode, sunset, sunrise);
    }
    
    /**
     * Converts a Unix timestamp to a LocalDateTime object.
     *
     * @param timestamp The timestamp to be converted.
     * @return The LocalDateTime object.
     */
    public LocalDateTime convertTimestampToDate(long timestamp) {
        Instant instant = Instant.ofEpochSecond(timestamp);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }
    
    /**
     * Retrieves and parses the hourly forecast data from JSON and returns it as a list of HourlyWeather objects.
     *
     * @return A list of HourlyWeather objects containing the parsed hourly forecast data.
     */
    public ArrayList<HourlyWeather> getHourlyWeather() {
        ArrayList<HourlyWeather> hourlyWeatherList = new ArrayList<>();
        
        JsonObject jsonObject = JsonParser.parseString(hourlyJsonData).getAsJsonObject();
        JsonArray hourlyList = jsonObject.getAsJsonArray("list"); // list of hours
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH");
        
        // Create HourlyWeather object and save it to hourly WeatherList for each hour
        for (JsonElement hourlyElement : hourlyList) {
            JsonObject hourlyObject = hourlyElement.getAsJsonObject();

            long timestamp = hourlyObject.getAsJsonPrimitive("dt").getAsLong();
            LocalDateTime time = convertTimestampToDate(timestamp);
            String formattedTime = time.format(formatter);

            double tempAsDouble = hourlyObject.getAsJsonObject("main").get("temp").getAsDouble();
            String temp = String.format("%.1f ℃", tempAsDouble);
            int weatherCode = hourlyObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("id").getAsInt();

            HourlyWeather hourlyWeather = new HourlyWeather(formattedTime, temp, weatherCode);
            hourlyWeatherList.add(hourlyWeather);
        }   
        return hourlyWeatherList;
    }
    
    /**
     * Retrieves and parses the daily forecast data from JSON and returns it as a list of DailyWeather objects.
     *
     * @return A list of DailyWeather objects containing the parsed daily forecast data.
     */
    public ArrayList<DailyWeather> getDailyWeather() {
        ArrayList<DailyWeather> dailyWeatherList = new ArrayList<>();
        
        JsonObject jsonObject = JsonParser.parseString(dailyJsonData).getAsJsonObject();
        JsonArray dailyList = jsonObject.getAsJsonArray("list"); // list of days
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.");

        // Create DailyWeather object and save to dailyWeatherList for each day
        for (JsonElement dailyElement : dailyList) {
            JsonObject dailyObject = dailyElement.getAsJsonObject();

            long timestamp = dailyObject.getAsJsonPrimitive("dt").getAsLong();
            LocalDateTime date = convertTimestampToDate(timestamp);
            String formattedDate = date.format(formatter);

            JsonObject tempObject = dailyObject.getAsJsonObject("temp");
            double maxTemp = tempObject.getAsJsonPrimitive("max").getAsDouble();
            double minTemp = tempObject.getAsJsonPrimitive("min").getAsDouble();
            
            JsonObject weatherObject = dailyObject.getAsJsonArray("weather").get(0).getAsJsonObject();
            String description = weatherObject.get("description").getAsString();
            int weatherCode = weatherObject.get("id").getAsInt();

            DailyWeather dailyWeatherData = new DailyWeather(formattedDate, maxTemp, minTemp, description, weatherCode);
            dailyWeatherList.add(dailyWeatherData);
        }
        return dailyWeatherList;  
    }

    /**
     * Writes the stored data to a file with the specified name.
     *
     * @param fileName The name of the file to which data will be written.
     * @return True if writing is successful, false otherwise.
     * @throws IOException If an I/O error occurs.
     */
    @Override
    public boolean writeToFile(String fileName) throws IOException {
        if (dataToWrite == null) {
            throw new IllegalStateException("No data set to write.");
        }

        String folderName = "weatherData";
        Path filePath = Paths.get(folderName, fileName);

        try {
            Files.createDirectories(filePath.getParent());  // Make sure the folder exists
            Files.writeString(filePath, dataToWrite, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            return true;
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
            return false;
        }
    }
}
