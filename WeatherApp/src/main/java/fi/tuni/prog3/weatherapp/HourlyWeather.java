package fi.tuni.prog3.weatherapp;

/**
 * Represents the weather conditions for a specific hour.
 */
public class HourlyWeather {
    private final String time;
    private final String temp;
    private final int weatherCode;
    
     /**
     * Constructs a new instance of HourlyWeather with the specified parameters.
     *
     * @param time        The time for which the weather is recorded.
     * @param temp        The temperature for the hour.
     * @param weatherCode The weather condition code for the hour.
     */
    public HourlyWeather(String time, String temp, int weatherCode) {
        this.time = time;
        this.temp = temp;
        this.weatherCode = weatherCode;
    }

     /**
     * Gets the time for which the weather is recorded.
     *
     * @return The time in string format.
     */
    public String getTime() {
        return time;
    }
    
    /**
     * Gets the temperature for the hour.
     *
     * @return The temperature in string format.
     */
    public String getTemp() {
        return temp;
    }
    
    /**
     * Gets the weather condition code for the hour.
     *
     * @return The weather condition code.
     */
    public int getWeatherCode() {
        return weatherCode;
    }  
}
