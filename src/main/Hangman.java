package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Hangman {
    public static final int MAX_TRAILS = 3;
    public int remainingTrials;
    public double score;
    Set<String> usedWordsSet = new HashSet<>();
    List<String> wordsList = new ArrayList<>();
    public int countAlphabet(String word, char alphabet) {
        int result = 0;
        for (char c: word.toCharArray()) {
            if(c == alphabet) result++;
        }
        return result;
    }

    public String fetchWord(int requestedLength) {
        remainingTrials = MAX_TRAILS;
        for (String result : wordsList) {
            if(result.length() != requestedLength) continue;
            else if (usedWordsSet.add(result)) return  result;
        }
        return  null;
    }

    public  void loadWords(){
        String word = null;
        try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\apabbathi2\\TestDrivenDevelopment\\src\\main\\WordSource.txt"))){
            while ((word = br.readLine()) != null){
                wordsList.add(word);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String fetchClue(String word) {
        StringBuilder clue = new StringBuilder();
        for (int i = 0; i < word.length(); i++) {
            clue.append("-");
        }
        return clue.toString();
    }

    public String fetchClue(String word, String clue, char guess) {
        remainingTrials--;
        if(guess >= 'A' && guess <= 'Z') guess += 32;
        if(guess < 'a' || guess > 'z') throw new IllegalArgumentException("Invalid Characters");
        StringBuilder newClue = new StringBuilder();
        for (int i = 0; i < word.length(); i++) {
            if(guess == word.charAt(i) && guess != clue.charAt(i)) {
                newClue.append(guess);
                score += (double)MAX_TRAILS / word.length();
            }
            else
                newClue.append(clue.charAt(i));
        }
        return newClue.toString();
    }
}
