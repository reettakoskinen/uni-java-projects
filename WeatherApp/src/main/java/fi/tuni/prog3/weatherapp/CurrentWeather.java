package fi.tuni.prog3.weatherapp;

/**
 * Represents the current weather information for a specific location.
 */
public class CurrentWeather {
    private final double currentTemp;
    private final double feelsLike;
    private final int clouds;
    private final int humidity;
    public final String cityName;
    private final double rain;
    private final double wind;
    private final String description;
    private final int weatherCode;
    private final String sunset;
    private final String sunrise;
    
    /**
     * Constructs a new instance of CurrentWeather with the specified parameters.
     *
     * @param currentTemp   The current temperature.
     * @param feelsLike     The temperature that it feels like.
     * @param clouds        The cloudiness percentage.
     * @param humidity      The humidity percentage.
     * @param cityName      The name of the city.
     * @param rain          The amount of rainfall.
     * @param wind          The wind speed.
     * @param description   The weather description.
     * @param weatherCode   The weather condition code.
     * @param sunset        The time of sunset.
     * @param sunrise       The time of sunrise.
     */
    public CurrentWeather(double currentTemp, double feelsLike, int clouds, int humidity, String cityName, double rain, double wind, String description, int weatherCode, String sunset, String sunrise) {
        this.currentTemp = currentTemp;
        this.feelsLike = feelsLike;
        this.clouds = clouds;
        this.humidity = humidity;
        this.cityName = cityName;
        this.rain = rain;
        this.wind = wind;
        this.description = description;
        this.weatherCode = weatherCode;
        this.sunset = sunset;
        this.sunrise = sunrise;
    }

    /**
     * Gets the current temperature.
     *
     * @return The current temperature in Celsius.
     */
    public double getCurrentTemp() {
        return currentTemp;
    }

    /**
     * Gets the "feels like" temperature.
     *
     * @return The "feels like" temperature in Celsius.
     */
    public double getFeelsLike() {
        return feelsLike;
    }

    /**
     * Gets the cloud cover percentage.
     *
     * @return The percentage of cloud cover.
     */
    public int getClouds() {
        return clouds;
    }

    /**
     * Gets the humidity percentage.
     *
     * @return The humidity percentage.
     */
    public int getHumidity() {
        return humidity;
    }
    
    /**
     * Gets the name of the city in uppercase.
     *
     * @return The name of the city in uppercase.
     */
    public String getCityName() {
        return cityName.toUpperCase();
    }
    
    /**
     * Gets the amount of rain in the last hour.
     *
     * @return The amount of rain in millimeters.
     */
    public double getRain() {
        return rain;
    }
    
    /**
     * Gets the wind speed.
     *
     * @return The wind speed in meters per second.
     */
    public double getWind() {
        return wind;
    }
    
    /**
     * Gets the description of the weather conditions.
     *
     * @return The weather condition description.
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Gets the weather code.
     *
     * @return The weather code.
     */
    public int getWeatherCode() {
        return weatherCode;
    }

    /**
     * Gets the time of sunset formatted as "HH:mm".
     *
     * @return The time of sunset.
     */
    public String getSunset() {
        return sunset;
    }

    /**
     * Gets the time of sunrise formatted as "HH:mm".
     *
     * @return The time of sunrise.
     */
    public String getSunrise() {
        return sunrise;
    }
}
