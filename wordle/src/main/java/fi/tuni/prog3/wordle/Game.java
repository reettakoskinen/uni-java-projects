/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fi.tuni.prog3.wordle;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author reett
 */
public class Game {
    private String word;
    
    public Game() {
    }
    
    public void chooseWord(int wordIndex) {
        String wordsFile = "words.txt";
        try {
            // Read all words from file 
            java.nio.file.Path filePath = Paths.get(wordsFile);
            List<String> words = Files.readAllLines(filePath);
            
            // Choose next word  
            if (!words.isEmpty()) {
                word = words.get(wordIndex).toUpperCase();
            }
            
        } catch (IOException e) {
                e.printStackTrace();
        }
    }
    
    public String getWord(int wordIndex) {
        chooseWord(wordIndex);
        return word;
    }
    
    public Map<Integer, String> guess(String guessedWord) {        
        Map<Integer, String> colorMap = new HashMap<>();
        
        // compare the word the user guessed to the correct word
        for (int i = 0; i < word.length(); i++) {
            char wordChar = word.charAt(i);
            char guessedChar = guessedWord.charAt(i);
            
            if (wordChar == guessedChar) {
                colorMap.put(i, "GREEN");
            }
            else if (word.contains(String.valueOf(guessedChar))) {
                colorMap.put(i, "ORANGE");
            }
            else {
                colorMap.put(i, "GRAY");
            }
        }
        return colorMap;
    }
    
    public boolean correctGuess(String guessedWord) {
        if (guessedWord.equals(word)) {
            return true;
        }
        return false;
    }
}
