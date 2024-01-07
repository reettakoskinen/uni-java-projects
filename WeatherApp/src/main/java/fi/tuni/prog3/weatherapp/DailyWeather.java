package fi.tuni.prog3.weatherapp;

/**
 * Represents the weather conditions for a specific date.
 */
public class DailyWeather {
    private final String date;
    private final double maxTemp;
    private final double minTemp;
    private final String description;
    private final int weatherCode;
    
     /**
     * Constructs a new instance of DailyWeather with the specified parameters.
     *
     * @param date         The date for which the weather is recorded.
     * @param maxTemp      The maximum temperature for the day.
     * @param minTemp      The minimum temperature for the day.
     * @param description  The weather description for the day.
     * @param weatherCode  The weather condition code for the day.
     */
    public DailyWeather(String date, double maxTemp, double minTemp, String description, int weatherCode) {
        this.date = date;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.description = description;
        this.weatherCode = weatherCode;
    }
    
     /**
     * Gets the date for which the weather is recorded.
     *
     * @return The date in string format.
     */
    public String getDate() {
        return date;
    }

     /**
     * Gets the maximum temperature for the day.
     *
     * @return The maximum temperature.
     */
    public double getMaxTemp() {
        return maxTemp;
    }

     /**
     * Gets the minimum temperature for the day.
     *
     * @return The minimum temperature.
     */
    public double getMinTemp() {
        return minTemp;
    }
    
    /**
     * Gets the weather description for the day.
     *
     * @return The weather description.
     */
    public String getDescription() {
        return description;
    } 
    
    /**
     * Gets the weather condition code for the day.
     *
     * @return The weather condition code.
     */ 
    public int getWeatherCode() {
        return weatherCode;
    }
}
