package fi.tuni.prog3.wordle;

import java.util.Map;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;


/**
 * JavaFX App
 */
public class Wordle extends Application {
    private int lastColumn;
    private int lastRow;
    private String guessedWord = "";
    private boolean gameActive;
    private String word;
    private Label [][] allLabels; 
    private Scene scene;
    private Game game;
    private GridPane grid;
    private Button newGameButton;
    private Label infoLabel;
    private int gameNumber = 0;


    @Override
    public void start(Stage stage) {
        // Initiate a new game and ui elements 
        newGame(stage);
        
        // Set stage
        stage.setTitle("Wordle");
        stage.show();
 
    }
    
    private void handleKeyPressedEvent(KeyEvent event) {
        if (gameActive) {
            if (event.getCode() == KeyCode.ENTER) {
                if (lastColumn != word.length()) {
                    infoLabel.setText("Give a complete word before pressing Enter!" );
                }
                else {
                    // get letters from labels and save them to guessedWord
                    updateGuessedWord();

                    // change color of all labels according to the guess
                    changeColor();

                    if(gameOver()) {
                        gameActive = false;
                    }
                    else {
                        // move to the next row
                        lastRow++;
                        lastColumn = 0;
                        guessedWord = "";
                    }
                    scene.getRoot().requestFocus();
                }
            }
            else if (event.getCode() == KeyCode.BACK_SPACE) {
                if (lastColumn != 0) {
                    allLabels[lastRow][--lastColumn].setText("");
                }
            }
        }
    }
    
    private void handleKeyTypedEvent(KeyEvent event) {
        if (gameActive) {
            String typedCharacter = event.getCharacter();

            if (Character.isLetter(typedCharacter.charAt(0))) {
                infoLabel.setText("");
                typedCharacter = typedCharacter.toUpperCase();

                if (lastColumn != word.length()) {
                    // Update the label's text with the typed character 
                    allLabels[lastRow][lastColumn].setText(typedCharacter);   
                    lastColumn++;
                }
            }
        }
    }
    
    private void updateGuessedWord() {
        for (int i = 0; i < allLabels[lastRow].length; i++) {
            Label currentLabel = allLabels[lastRow][i];
            String character = currentLabel.getText();
            guessedWord += character;
        }
    }
    
    private void changeColor() {
        // check what letters were guessed correctly
        Map<Integer, String> colorMap = game.guess(guessedWord); 
        
        for (int i = 0; i < word.length(); i++) {
            Label currentLabel = allLabels[lastRow][i];
            String color = colorMap.get(i);
            if (color.equals("GREEN")) {
                BackgroundFill backgroundFill = new BackgroundFill(
                        Color.GREEN, null, null);
                Background background = new Background(backgroundFill);
                currentLabel.setBackground(background);
            }
            else if (color.equals("ORANGE")) {
                BackgroundFill backgroundFill = new BackgroundFill(
                        Color.ORANGE, null, null);
                Background background = new Background(backgroundFill);
                currentLabel.setBackground(background);
            }
            else if (color.equals("GRAY")) {
                BackgroundFill backgroundFill = new BackgroundFill(
                        Color.GRAY, null, null);
                Background background = new Background(backgroundFill);
                currentLabel.setBackground(background);
                //currentLabel.setTextFill(Color.GRAY);
            }
        }
    }
    
    private boolean gameOver() {
        if (game.correctGuess(guessedWord)) {
            infoLabel.setText("Congratulations, you won!");
            return true;
        }
        else if (lastRow == 5) {
            infoLabel.setText("Game over, you lost!");
            return true;
        }
        return false;
    }
    
    private void newGame(Stage stage) {        
        game = new Game();
        word = game.getWord(gameNumber);
        gameNumber++; // gameNumber is increased to get a new word in next game
        gameActive = true;
        allLabels = new Label[6][word.length()];
        lastColumn = 0;
        lastRow = 0;
        guessedWord = "";
             
        // Background for letter labels
        BackgroundFill whiteBackgroundFill = new BackgroundFill(
                Color.WHITE, null, null);
        Background whiteBackground = new Background(whiteBackgroundFill);
        
        // Border for letter labels
        Border border = new Border(new BorderStroke(
                Color.BLACK, BorderStrokeStyle.SOLID, 
                CornerRadii.EMPTY, new BorderWidths(2)));
        
         grid = new GridPane();
         
        // Create grid of labels   
        for (int r = 0; r < 6; r++) {
            for (int c = 0; c < word.length(); c++) {
                Label letterLabel = new Label();
                letterLabel.setBackground(whiteBackground);
                letterLabel.setBorder(border);
                letterLabel.setPrefSize(100, 100);
                letterLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
                letterLabel.setAlignment(Pos.CENTER);
                String id = String.format("%d_%d", r,c);
                letterLabel.setId(id);
                grid.add(letterLabel, c+1, r+1);
                allLabels [r][c] = letterLabel;
            }
        }
        
        // Create constant game elemets
        newGameButton = new Button();
        newGameButton.setText("Start new game");
        newGameButton.setPrefSize(100, 50);
        newGameButton.setId("newGameBtn");
        grid.add(newGameButton, 0, 0);
        newGameButton.setOnAction((event) -> {
            newGame(stage);
        });
        
        infoLabel = new Label();
        infoLabel.setId("infoBox");
        infoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        grid.add(infoLabel, 1,0, 3, 1);
        
        // Create scene and add grid to it
        int gridWidth = 100 * word.length() + 200;
        int gridHeight = 700;
        scene = new Scene(grid, gridWidth, gridHeight);
        
        // Set the new Scene to the stage
        stage.setScene(scene);
        scene.addEventHandler(KeyEvent.KEY_PRESSED, this::handleKeyPressedEvent);
        scene.addEventHandler(KeyEvent.KEY_TYPED, this::handleKeyTypedEvent);  
        scene.getRoot().requestFocus();
    }
    
    public static void main(String[] args) {
        launch();
    }

}