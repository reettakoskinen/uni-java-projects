package fi.tuni.prog3.weatherapp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.stream.Collectors;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * The main class for the WeatherApp application.
 */
public class WeatherApp extends Application {

    private Stage searchStage;
    private CurrentWeather currentWeather;
    private ArrayList<DailyWeather> dailyWeatherList;
    private ArrayList<HourlyWeather> hourlyWeatherList;
    private Label locationLabel, tempLabel, feelsLikeLabel, rainLabel, windLabel, humLabel, description, sunsetLabel, sunriseLabel;
    private ImageView icon;
    private HBox hourlySection = new HBox(10);
    private HBox dailySection = new HBox(10);
    private Menu favoritesMenu;
    private MenuBar menuBar;
    private Text secondWindowInfoText;
    private TextField cityTextField;
    private int currentTime, sunsetTime, sunriseTime;
    private boolean dayTime;

    /**
     * The main entry point for the application.
     *
     * @param stage The primary stage for the application.
     * @throws IOException If an error occurs during the application execution.
     */
    @Override
    public void start(Stage stage) throws IOException {
        // get the weather of the last searched city
        String lastSearchedCity = loadLastSearchedCity();
        if (lastSearchedCity == null || lastSearchedCity.isEmpty()) {
            lastSearchedCity = "Helsinki"; // Replace with a default city name
        }
        weatherApiImpl weatherApi = new weatherApiImpl();
        weatherApi.lookUpLocation(lastSearchedCity);
        ReadFile file = new ReadFile();
        file.readFromFile("weatherData");
        currentWeather = file.getCurrentWeather();
        dailyWeatherList = file.getDailyWeather();
        hourlyWeatherList = file.getHourlyWeather();
        
        // Check if it is day time or night time currently
        currentTime = Integer.parseInt(hourlyWeatherList.get(0).getTime());
        sunsetTime = Integer.parseInt(currentWeather.getSunset());
        sunriseTime = Integer.parseInt(currentWeather.getSunrise());
              
        dayTime = currentTime <= sunsetTime && currentTime >= sunriseTime; 

        // Create the main sections
        HBox menuSection = createMenuSection();
        HBox currentSection = createCurrentSection();
        hourlySection = createHourlySection();
        dailySection = createDailySection();

        // Create a horizontal scrollbar that is always shown for hourlySection
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(hourlySection);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        StackPane scrollPaneContainer = new StackPane(scrollPane);

        // Create a VBox to hold all the sections
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));
        
        // Set the color depending on the time of day
        if (dayTime) {  
            scrollPaneContainer.setStyle(
                "-fx-background-radius: 10; " +
                "-fx-background-color: #a2e0eb; " +
                "-fx-border-color: #a2e0eb; " + 
                "-fx-border-width: 3; " +
                "-fx-border-radius: 10;"
            );
            root.setStyle("-fx-background-color: linear-gradient(to bottom, #29B6F6, #E0F7FA);");
        }
        else {
            scrollPaneContainer.setStyle(
                "-fx-background-radius: 10; " +
                "-fx-background-color: #5b75c0; " +
                "-fx-border-color: #5b75c0; " + 
                "-fx-border-width: 3; " +
                "-fx-border-radius: 10;"
            );
            root.setStyle("-fx-background-color: linear-gradient(to bottom, #0D47A1, #512DA8);");
        }
        
        // Add all the sections to the window
        root.getChildren().addAll(menuSection, currentSection, hourlySection, scrollPaneContainer, dailySection);

        // Create the scene
        Scene scene = new Scene(root, 600, 750);

        // Set the stage properties
        stage.setTitle("WeatherApp");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Method to create the top menu section of the UI.
     *
     * @return HBox containing the menu section.
     */
    private HBox createMenuSection() {
        Button search = getSearchButton();
        Button quit = getQuitButton();
        HBox menuSection = new HBox(10);
        menuSection.setPadding(new Insets(10));
        menuSection.getChildren().addAll(search, quit);
        menuSection.setPrefHeight(40);

        return menuSection;
    }

    /**
     * Method to create the current weather section of the UI.
     *
     * @return HBox containing the current weather section.
     */
    private HBox createCurrentSection() {
        // Create labels and icon
        locationLabel = new Label();
        locationLabel.setStyle("-fx-font: 30 Calibri;");
        locationLabel.setTextFill(Color.WHITE);
        tempLabel = new Label();
        tempLabel.setStyle("-fx-font: 40 Calibri;");
        tempLabel.setTextFill(Color.WHITE);
        feelsLikeLabel = new Label();
        feelsLikeLabel.setTextFill(Color.WHITE);
        rainLabel = new Label();
        rainLabel.setTextFill(Color.WHITE);
        windLabel = new Label();
        windLabel.setTextFill(Color.WHITE);
        humLabel = new Label();
        humLabel.setTextFill(Color.WHITE);
        description = new Label();
        description.setTextFill(Color.WHITE);
        sunsetLabel = new Label();
        sunsetLabel.setTextFill(Color.WHITE);
        sunriseLabel = new Label();
        sunriseLabel.setTextFill(Color.WHITE);
        icon = getIcon(currentWeather.getWeatherCode());

        // Create a grid to hold the labels
        GridPane currentInfo = new GridPane();
        currentInfo.setHgap(10);
        currentInfo.setVgap(10);
        currentInfo.setPrefHeight(250);
        currentInfo.setAlignment(Pos.CENTER);

        // Add items to column, row (column span, row span) of the grid
        currentInfo.add(locationLabel, 0, 0, 3, 1);
        currentInfo.add(tempLabel, 0, 1, 3, 1);
        currentInfo.add(feelsLikeLabel, 0, 2, 3, 1);
        currentInfo.add(description, 0, 3, 3, 1);
        currentInfo.add(rainLabel, 0, 4);
        currentInfo.add(windLabel, 1, 4);
        currentInfo.add(humLabel, 2, 4);
        currentInfo.add(sunriseLabel, 0, 5);
        currentInfo.add(sunsetLabel, 1, 5);

        // Create a HBox to hold the icon and currentInfo grid
        HBox currentSection = new HBox(30);
        currentSection.setPadding(new Insets(10));
        currentSection.getChildren().addAll(icon, currentInfo);
        currentSection.setPrefHeight(300);
        currentSection.setAlignment(Pos.CENTER);

        updateAllWeatherSections();

        return currentSection;

    }

    /**
     * Method to create the hourly weather section of the UI.
     *
     * @return HBox containing the hourly weather section.
     */
    private HBox createHourlySection() {
        hourlySection.setPadding(new Insets(10));

        // Create a VBox for each hour with hourly data and add them to the section
        for (HourlyWeather data : hourlyWeatherList) {
            VBox hour = new VBox(10);
            Label timeLabel = new Label();
            timeLabel.setStyle("-fx-font: 18 Calibri;");
            ImageView hourlyIcon = getIcon(data.getWeatherCode());
            hourlyIcon.setFitWidth(40);
            hourlyIcon.setFitHeight(40);
            Label hourlyTempLabel = new Label();
            hourlyTempLabel.setStyle("-fx-font: 18 Calibri;");
            hourlyTempLabel.setMinWidth(70);
            
            hour.getChildren().addAll(timeLabel, hourlyIcon, hourlyTempLabel);
            hour.setAlignment(Pos.CENTER);
            hourlySection.getChildren().add(hour);
        }

        // Set style
        hourlySection.setPrefWidth(80 * 24);
        hourlySection.setPrefHeight(138);
        if (dayTime) {
            hourlySection.setStyle("-fx-background-radius: 10; -fx-background-color: #9edffb");
        }
        else {
            hourlySection.setStyle("-fx-background-radius: 10; -fx-background-color: #5b75c0");
        }
        
        updateAllWeatherSections();
        return hourlySection;
    }
    
        /**
     * Method to create the daily weather section of the UI.
     *
     * @return HBox containing the daily weather section.
     */
    private HBox createDailySection() {
        // Create Hbox to be the main structure
        dailySection.setPadding(new Insets(10));
        dailySection.setSpacing(25);

        // Create a VBox for each day with daily data and add them to the HBox
        for (DailyWeather data : dailyWeatherList) {
            VBox day = new VBox(10);
            Label dateLabel = new Label();
            dateLabel.setStyle("-fx-font: 18 Calibri;");
            ImageView dailyIcon = getIcon(data.getWeatherCode());
            dailyIcon.setFitWidth(60);
            dailyIcon.setFitHeight(60);
            Label maxLabel = new Label();
            Label minLabel = new Label();
            maxLabel.setStyle("-fx-font: 14 Calibri;");
            minLabel.setStyle("-fx-font: 14 Calibri;");
            day.getChildren().addAll(dateLabel, dailyIcon, minLabel ,maxLabel);
            day.setAlignment(Pos.CENTER);
            if (dayTime) {
                day.setStyle("-fx-background-radius: 10; -fx-background-color: rgba(79, 195, 247, 0.7);");
            }
            else {
                day.setStyle("-fx-background-radius: 10; -fx-background-color: rgba(159, 168, 218, 0.5);");
            }
            day.setPadding(new Insets(15));
            dailySection.getChildren().add(day);
        }

        // Set style
        dailySection.setPrefHeight(180);
        dailySection.setAlignment(Pos.CENTER);

        updateAllWeatherSections();

        return dailySection;
    }

    /**
     * Update the content of all weather sections with new data.
     */
    private void updateAllWeatherSections() {
        // Update hourly content with new data
        if (hourlyWeatherList != null && !hourlyWeatherList.isEmpty()) {
            int index = 0;
            for (HourlyWeather data : hourlyWeatherList) {
                if (index < hourlySection.getChildren().size()) {
                    VBox hour = (VBox) hourlySection.getChildren().get(index);

                    // Update existing labels and icon
                    Label timeLabel = (Label) hour.getChildren().get(0);
                    timeLabel.setText(data.getTime());

                    ImageView hourlyIcon = (ImageView) hour.getChildren().get(1);
                    hourlyIcon.setImage(getIcon(data.getWeatherCode()).getImage());

                    Label hourlyTempLabel = (Label) hour.getChildren().get(2);
                    hourlyTempLabel.setText(data.getTemp());
                    hourlyTempLabel.setTooltip(new Tooltip(data.getTemp()));

                    index++;
                } else {
                    // Handle the case where there's not enough existing VBox elements for the new data
                    break;
                }
            }
        }
        
        // Update daily content with new data
        if (dailyWeatherList != null && !dailyWeatherList.isEmpty()) {
            int index = 0;
            for (DailyWeather data : dailyWeatherList) {
                if (index < dailySection.getChildren().size()) {
                    VBox day = (VBox) dailySection.getChildren().get(index);

                    // Update existing labels and icon
                    Label dateLabel = (Label) day.getChildren().get(0);
                    dateLabel.setText(data.getDate());

                    ImageView dailyIcon = (ImageView) day.getChildren().get(1);
                    dailyIcon.setImage(getIcon(data.getWeatherCode()).getImage());
                    
                    Label minLabel = (Label) day.getChildren().get(2);
                    minLabel.setText(String.format("min: %.1f", data.getMinTemp()));
                        
                    Label maxLabel = (Label) day.getChildren().get(3);
                    maxLabel.setText(String.format("max: %.1f", data.getMaxTemp()));

                    index++;
                } else {
                    // Handle the case where there's not enough existing VBox elements for the new data
                    break;
                }
            }
        }
        
        // Update current content with new data
        if (currentWeather != null) {
            locationLabel.setText(currentWeather.getCityName());
            tempLabel.setText(String.format("%.1f ℃", currentWeather.getCurrentTemp()));
            feelsLikeLabel.setText(String.format("Feels like %.1f ℃", currentWeather.getFeelsLike()));
            rainLabel.setText(String.format("Rain: %.1f mm", currentWeather.getRain()));
            windLabel.setText(String.format("Wind: %.1f m/s", currentWeather.getWind()));
            humLabel.setText(String.format("Humidity: %d %%", currentWeather.getHumidity()));
            description.setText(currentWeather.getDescription());
            sunsetLabel.setText(String.format("Sunset at %d", sunsetTime));
            sunriseLabel.setText(String.format("Sunrise at %d", sunriseTime));
            icon.setImage(getIcon(currentWeather.getWeatherCode()).getImage());
        }        
    }


    /**
     * Save the last searched city to a file.
     *
     * @param cityName The name of the city to be saved.
     */
    void saveLastSearchedCity(String cityName) {
        try {
            Path filePath = Paths.get("lastSearchedCity.txt");
            Files.writeString(filePath, cityName);
        } catch (IOException e) {
            System.err.println("Error saving last searched city: " + e.getMessage());
        }
    }

    /**
     * Load the last searched city from a file.
     *
     * @return The last searched city.
     */
    String loadLastSearchedCity() {
        try {
            Path filePath = Paths.get("lastSearchedCity.txt");
            if (Files.exists(filePath)) {
                return Files.readString(filePath).trim();
            }
        } catch (IOException e) {
            System.err.println("Error loading searched city: " + e.getMessage());
        }
        return null;
    }

    /**
     * Get the appropriate weather icon based on the weather code.
     *
     * @param weatherCode The code representing the weather conditions.
     * @return ImageView containing the weather icon.
     */
    ImageView getIcon(int weatherCode) {
        // Categorize weathers
        List<Integer> lightrainthunderstorm = new ArrayList<>(Arrays.asList(200, 230, 231));
        List<Integer> heavythunderstorm = new ArrayList<>(Arrays.asList(201, 202, 211, 212, 221, 232));
        List<Integer> lightthunderstorm = new ArrayList<>(Arrays.asList(210));
        List<Integer> drizzle = new ArrayList<>(Arrays.asList(300, 301, 302, 310, 311, 313, 321));
        List<Integer> lightrain = new ArrayList<>(Arrays.asList(500));
        List<Integer> rain = new ArrayList<>(Arrays.asList(312, 314, 501, 520, 521));
        List<Integer> heavyrain = new ArrayList<>(Arrays.asList(502, 503, 504, 522, 531));
        List<Integer> heavysnow = new ArrayList<>(Arrays.asList(602, 621, 622));
        List<Integer> lightsnow = new ArrayList<>(Arrays.asList(600, 601, 620));
        List<Integer> sleet = new ArrayList<>(Arrays.asList(611, 612, 613, 615, 616));
        List<Integer> atmosphere = new ArrayList<>(Arrays.asList(711, 721, 771));
        List<Integer> clouds = new ArrayList<>(Arrays.asList(804));
        List<Integer> partlycloudy = new ArrayList<>(Arrays.asList(802, 803));
        List<Integer> mostlysunny = new ArrayList<>(Arrays.asList(801));
        List<Integer> mistandfog = new ArrayList<>(Arrays.asList(701, 741));
        List<Integer> tornado = new ArrayList<>(Arrays.asList(781));
        List<Integer> duststorm = new ArrayList<>(Arrays.asList(731, 751, 761, 762));

        // clear sky by defalt 
        Image weatherIcon = new Image(new File("icons/sun.png").toURI().toString());

        if (clouds.contains(weatherCode)) {
            weatherIcon = new Image(new File("icons/cloud.png").toURI().toString());
        } else if (heavysnow.contains(weatherCode)) {
            weatherIcon = new Image(new File("icons/snowy.png").toURI().toString());
        } else if (lightsnow.contains(weatherCode)) {
            weatherIcon = new Image(new File("icons/light-snow.png").toURI().toString());
        } else if (partlycloudy.contains(weatherCode)) {
            weatherIcon = new Image(new File("icons/partly-cloudy.png").toURI().toString());
        } else if (mostlysunny.contains(weatherCode)) {
            weatherIcon = new Image(new File("icons/mostlysunny.png").toURI().toString());  
        } else if (drizzle.contains(weatherCode)) {
            weatherIcon = new Image(new File("icons/drizzle.png").toURI().toString());
        } else if (lightrain.contains(weatherCode)) {
            weatherIcon = new Image(new File("icons/light-rain.png").toURI().toString());
        } else if (rain.contains(weatherCode)) {
            weatherIcon = new Image(new File("icons/rain.png").toURI().toString());
        } else if (heavyrain.contains(weatherCode)) {
            weatherIcon = new Image(new File("icons/heavy-rain.png").toURI().toString());      
        } else if (mistandfog.contains(weatherCode)) {
            weatherIcon = new Image(new File("icons/mist.png").toURI().toString());
        } else if (lightrainthunderstorm.contains(weatherCode)) {
            weatherIcon = new Image(new File("icons/storm-light-rain.png").toURI().toString());
        } else if (heavythunderstorm.contains(weatherCode)) {
            weatherIcon = new Image(new File("icons/storm-with-heavy-rain.png").toURI().toString());
        } else if (lightthunderstorm.contains(weatherCode)) {
            weatherIcon = new Image(new File("icons/storm-light.png").toURI().toString());
        } else if (sleet.contains(weatherCode)) {
            weatherIcon = new Image(new File("icons/sleet.png").toURI().toString());
        } else if (atmosphere.contains(weatherCode)) {
            weatherIcon = new Image(new File("icons/smoke.png").toURI().toString());
        } else if (tornado.contains(weatherCode)) {
            weatherIcon = new Image(new File("icons/hurricane.png").toURI().toString());
        } else if (duststorm.contains(weatherCode)) {
            weatherIcon = new Image(new File("icons/dust-storm.png").toURI().toString());
        }

        ImageView imageView = new ImageView(weatherIcon);
        imageView.setFitWidth(200);
        imageView.setFitHeight(200);

        return imageView;
    }

    /**
     * Get the Quit button for the UI.
     *
     * @return Button for quitting the application.
     */
    Button getQuitButton() {
        Button quitButton = new Button("Quit");
        quitButton.setOnAction((ActionEvent event) -> {
            Platform.exit();
        });

        return quitButton;
    }

    /**
     * Get the Search button for the UI.
     *
     * @return Button for searching and accessing favourites.
     */
    Button getSearchButton() {
        Button searchButton = new Button("Search and Favorites");
        searchButton.setOnAction(e -> openSearchWindow());

        return searchButton;
    }

    /**
    * Opens the search window, allowing the user to enter a city name for weather information.
    * If the window does not exist, it creates a new stage; otherwise, it clears the text field.
    * The method handles searching for a city, adding it to favourites, and updating UI elements.
    */
    private void openSearchWindow() {
        // Check if the searchStage is already created
        if (searchStage == null) {
            searchStage = new Stage();

            // Top section: TextField and Button
            VBox topSection = new VBox(10);
            topSection.setAlignment(Pos.CENTER);
            
            // Info text for error cases
            secondWindowInfoText = new Text("");
            secondWindowInfoText.setFill(Color.RED);
            
            // Create TextField for city name input
            cityTextField = new TextField();
            cityTextField.setPromptText("Enter city name");
            cityTextField.setMaxWidth(200);

            // Create Search Button and set it on action
            Button searchButton = new Button("Search");
            searchButton.setStyle("-fx-background-color: lightblue; -fx-background-radius: 10;");
            searchButton.setOnAction(e -> {
                // Get city name from the TextField, perform search, and close the window
                String cityName = cityTextField.getText().trim(); // Trim to remove whitespace
                if (!cityName.isEmpty()) {
                    performSearch(cityName);
                    secondWindowInfoText.setText("");
                    searchStage.close();
                } else {
                    secondWindowInfoText.setText("Please type a city name.");
                }
            });

            // Create "Add to Favorites" Button and set it on action
            Button addToFavoritesButton = new Button("Add to Favorites");
            addToFavoritesButton.setStyle("-fx-background-color: lightblue; -fx-background-radius: 10;");
            addToFavoritesButton.setOnAction(e -> {
                // Get city name from the TextField and add it to favorites
                String cityName = cityTextField.getText();
                if (cityName.isEmpty()) {
                    Platform.runLater(() -> secondWindowInfoText.setText("Please type a city name."));
                } else {
                    addToFavorites(cityName);
                    secondWindowInfoText.setText("Favorite successfully saved!");
                }
            });

            // Create an HBox for buttons
            HBox buttonLayout = new HBox(10, searchButton, addToFavoritesButton);
            buttonLayout.setAlignment(Pos.CENTER); // Center align the buttons within the HBox

            // Initialize MenuBar and Menu for favorites
            favoritesMenu = new Menu("Favorites");
            menuBar = new MenuBar();
            menuBar.getMenus().add(favoritesMenu);

            loadFavorites();

            // Wrap the MenuBar in an HBox for better alignment and sizing
            HBox menuBarContainer = new HBox(menuBar);
            menuBarContainer.setAlignment(Pos.CENTER); 
            menuBar.setMaxWidth(80); 

            topSection.getChildren().addAll(secondWindowInfoText, cityTextField, buttonLayout, menuBar);

            // Main layout
            VBox mainLayout = new VBox(30);
            mainLayout.getChildren().add(topSection);
            mainLayout.setStyle("-fx-background-color: white; -fx-padding: 10;");

            Scene scene = new Scene(mainLayout, 300, 300);
            searchStage.setScene(scene);
            searchStage.setTitle("City Search & Favorites:");

            Platform.runLater(() -> mainLayout.requestFocus());
        } else {
            // Clear the text field if the window already exists
            cityTextField.setText("");
        }
        // Show the stage without re-creating the UI components
        searchStage.show();
    }
    
    /**
    * Loads favourite cities from the "favourites" file and populates the favoritesMenu.
    * Clears existing items in the menu before adding the loaded favourites.
    */
    private void loadFavorites() {
        try {
            // Read favorite cities from file and populate the favoritesMenu
            List<String> favorites = Files.readAllLines(Paths.get("favorites"));
            favoritesMenu.getItems().clear(); // Clear existing items
            for (String city : favorites) {
                MenuItem menuItem = new MenuItem(city);
                menuItem.setOnAction(e -> cityTextField.setText(city)); // Set text field on menu item selection
                favoritesMenu.getItems().add(menuItem);
            }
        } catch (IOException e) {
            System.err.println("Error loading favorites: " + e.getMessage());

        }
    }

    /**
    * Adds the specified city to the favourites list, saving it to the "favourites" file.
    * Checks if the city is already a favourite before adding, and updates UI accordingly.
    * Handles exceptions and displays appropriate messages.
    * @param cityName The name of the city to be added to favourites.
    */
    private void addToFavorites(String cityName) {
        try {
            // Create or update the favorites file with the new city name
            Path filePath = Paths.get("favorites");
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
            }

            String standardizedCityName = cityName.trim().toLowerCase();
            List<String> favorites = Files.readAllLines(filePath);

            List<String> standardizedFavorites = favorites.stream()
                    .map(String::trim)
                    .map(String::toLowerCase)
                    .collect(Collectors.toList());
            
            // Check if the city is not already in favorites, then add it
            if (!standardizedFavorites.contains(standardizedCityName)) {
                Files.writeString(filePath, cityName + System.lineSeparator(), StandardOpenOption.APPEND);
                loadFavorites();
                Platform.runLater(() -> secondWindowInfoText.setText("Favorite successfully saved!"));
            } else {
                Platform.runLater(() -> secondWindowInfoText.setText("City is already a favorite."));
            }
        } catch (IOException ex) {
            Platform.runLater(() -> secondWindowInfoText.setText("Error: " + ex.getMessage()));
        }
    }

    /**
    * Performs a weather search for the specified city name using the OpenWeatherMap API.
    * Reads the weather data from a file, updates the UI with the new data, and saves the last searched city.
    * Handles exceptions that may occur during the search or file operations.
    * @param cityName The name of the city for which weather information is to be retrieved.
    */
    private void performSearch(String cityName) {
        // Use the weather API to look up location data for the given city name
        weatherApiImpl weatherApi = new weatherApiImpl();
        weatherApi.lookUpLocation(cityName);

        try {
            // Read weather data from file and update the UI components
            ReadFile file = new ReadFile();
            file.readFromFile("weatherData");

            hourlyWeatherList = file.getHourlyWeather();
            dailyWeatherList = file.getDailyWeather();
            currentWeather = file.getCurrentWeather();
            Platform.runLater(this::updateAllWeatherSections);

        } catch (IOException e) {
            System.err.println("Error searching for a city: " + e.getMessage());
        }

        saveLastSearchedCity(cityName);
    }

    public static void main(String[] args) {
        launch();
    }
}
