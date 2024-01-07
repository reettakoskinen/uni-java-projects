package fi.tuni.prog3.weatherapp;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
* Implementation of the iAPI interface for interacting with the OpenWeatherMap API.
*/
public class weatherApiImpl implements iAPI {
    private final String apiKey = "b201aa400ba5bdc211c7bbf93e38162f"; 

    /**
     * Looks up the geographical coordinates (latitude and longitude) for a given location.
     *
     * @param loc The location for which coordinates are to be looked up.
     * @return A string containing the coordinates".
     */
    @Override
    public String lookUpLocation(String loc) {
        String coordinates = "";
        double latitude = 0.0;
        double longitude = 0.0;
        
        // Build the API URL
        String apiUrl = String.format("http://api.openweathermap.org/geo/1.0/direct?q=%s&limit=%d&appid=%s",
                loc, 1, apiKey);


        // Create an HttpClient
        HttpClient httpClient = HttpClient.newHttpClient();

        // Create an HttpRequest
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .build();
        
        try {
            // Send the request and get the response
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            // Check if the request was successful (status code 200)
            if (response.statusCode() == 200) {
                // Parse the JSON response using Gson
                JsonArray jsonArray = JsonParser.parseString(response.body()).getAsJsonArray();

                // Extract latitude and longitude directly
                latitude = jsonArray.get(0).getAsJsonObject().get("lat").getAsDouble();
                longitude = jsonArray.get(0).getAsJsonObject().get("lon").getAsDouble();
                
                coordinates = String.format("lat %f, lon %f", latitude, longitude);

            } else {
                System.out.println("Failed to get location information. Status code: " + response.statusCode());
            }
        } catch (JsonSyntaxException | IOException | InterruptedException e) {
            System.err.println("Error making the location API call: " + e.getMessage());
        }
        getCurrentWeather(latitude, longitude);
        getHourlyForecast(latitude, longitude);
        getDailyForecast(latitude, longitude);
        return coordinates;
    }

    /**
     * Retrieves the current weather data for a given set of coordinates.
     *
     * @param lat The latitude of the location.
     * @param lon The longitude of the location.
     * @return A string containing the JSON data for the current weather information.
     */
    @Override
    public String getCurrentWeather(double lat, double lon) {
        String currentJsonData = "";
        // Build the API URL
        String apiUrl = String.format("https://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&appid=%s&units=%s",
                lat, lon, apiKey, "metric");

        // Create an HttpClient
        HttpClient httpClient = HttpClient.newHttpClient();

        // Create an HttpRequest
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .build();
        
        try {
            // Send the request and get the response
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            // Check if the request was successful (status code 200)
            if (response.statusCode() == 200) {
                currentJsonData = response.body();
                
                ReadFile file = new ReadFile();
                file.setDataToWrite(currentJsonData);
                file.writeToFile("currentWeatherData");

            } else {
                System.out.println("Failed to get daily weather information. Status code: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Error making the current weather API call: " + e.getMessage());
        }
        return currentJsonData;
    }

    /**
     * Retrieves the hourly forecast data for a given set of coordinates.
     *
     * @param lat The latitude of the location.
     * @param lon The longitude of the location.
     * @return A string containing the JSON data for the hourly forecast information.
     */
    @Override
    public String getHourlyForecast(double lat, double lon) {
        String hourlyJsonData = "";
        // Build the API URL
        String apiUrl = String.format("https://pro.openweathermap.org/data/2.5/forecast/hourly?lat=%f&lon=%f&appid=%s&cnt=%d&units=%s",
                lat, lon, apiKey, 24, "metric"); // get hourly forcast for the next 24 hours

        // Create an HttpClient
        HttpClient httpClient = HttpClient.newHttpClient();

        // Create an HttpRequest
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .build();

        try {
            // Send the request and get the response
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            // Check if the request was successful (status code 200)
            if (response.statusCode() == 200) {
                hourlyJsonData = response.body();

                ReadFile file = new ReadFile();
                file.setDataToWrite(hourlyJsonData);
                file.writeToFile("hourlyWeatherData");

            } else {
                System.out.println("Failed to get hourly forecast information. Status code: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Error making the hourly weather API call: " + e.getMessage());
        }
        return hourlyJsonData;
}
   
    /**
     * Retrieves the daily forecast data for a given set of coordinates.
     *
     * @param lat The latitude of the location.
     * @param lon The longitude of the location.
     * @return A string containing the JSON data for the daily forecast information.
     */
    @Override
    public String getDailyForecast(double lat, double lon) {
                String dailyJsonData = "";
        // Build the API URL
        int numOfDays = 5;
        String apiUrl = String.format("https://api.openweathermap.org/data/2.5/forecast/daily?lat=%f&lon=%f&cnt=%d&appid=%s&units=%s",
                lat, lon, numOfDays, apiKey, "metric");

        // Create an HttpClient
        HttpClient httpClient = HttpClient.newHttpClient();

        // Create an HttpRequest
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .build();
        
        try {
            // Send the request and get the response
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            // Check if the request was successful (status code 200)
            if (response.statusCode() == 200) {
                dailyJsonData = response.body();
                
                ReadFile file = new ReadFile();
                file.setDataToWrite(dailyJsonData);
                file.writeToFile("dailyWeatherData");

            } else {
                System.out.println("Failed to get weather information. Status code: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Error making the daily weather API call: " + e.getMessage());
        }
        return dailyJsonData;
    }
}
