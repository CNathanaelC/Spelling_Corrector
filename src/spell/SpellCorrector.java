package spell;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public class SpellCorrector implements ISpellCorrector {
    private Trie dictionary = new Trie();
    @Override
    public void useDictionary(String dictionaryFileName) throws IOException {
        //Read in file
        File file = new File(dictionaryFileName);
        Scanner scanner = new Scanner(file);
        while (scanner.hasNext()) {
            String word = scanner.next();
            dictionary.add(word);
        }
    }
    @Override
    public String suggestSimilarWord(String inputWord) {
        StringBuilder ip = new StringBuilder();
        for(int i = 0; i < inputWord.length(); i++) {
            ip.append(Character.toLowerCase(inputWord.charAt(i)));
        }
        inputWord = ip.toString();
        List<String> possibleWords = new ArrayList<>();

        //Edit distance 1
        //Correctly Spelled Word
        if(dictionary.find(inputWord) != null) {
            if(dictionary.find(inputWord).getValue() > 0) {
                return inputWord;
            }
        }
        //Inserting
        insert(inputWord, possibleWords);
        //Deleting
        delete(inputWord, possibleWords);
        //Altering
        alter(inputWord, possibleWords);
        //Transposition
        transpose(inputWord, possibleWords);
        String suggestion = "";
        int suggestionCount = 0;
        for(String word : possibleWords) {
            if(dictionary.find(word) != null) {
                if(dictionary.find(word).getValue() > suggestionCount) {
                    suggestionCount = dictionary.find(word).getValue();
                    suggestion = word;
                }
            }
        }
        List<String> newPossibleWords = new ArrayList<>();
        if(suggestionCount == 0) {
            for(String word : possibleWords) {
                //Inserting
                insert(word, newPossibleWords);
                //Deleting
                delete(word, newPossibleWords);
                //Altering
                alter(word, newPossibleWords);
                //Transposition
                transpose(word, newPossibleWords);
            }
            suggestion = "";
            suggestionCount = 0;
            for(String word : newPossibleWords) {
                if(dictionary.find(word) != null) {
                    if(dictionary.find(word).getValue() > suggestionCount) {
                        suggestionCount = dictionary.find(word).getValue();
                        suggestion = word;
                    }
                }
            }
        }
        if(suggestionCount == 0) {
            return null;
        }
        return suggestion;
    }
    //Inserting one character
    public void insert(String inputWord, List<String> possibleWords) {
        char[] alphabet = new char[26];
        for (int i = 0; i < 26; i++) {
            alphabet[i] = (char)(i+'a');
        }
        for(int i = 0; i <= inputWord.length(); i++) {
            for (int j = 0; j < 26; j++) {
                StringBuilder editWord = new StringBuilder();
                editWord.append(inputWord);
                editWord.insert(i, alphabet[j]);
                possibleWords.add(editWord.toString());
            }
        }
        for(int i = 0; i <= inputWord.length(); i++) {
            for (int j = 0; j < 26; j++) {
                StringBuilder editWord = new StringBuilder();
                editWord.append(inputWord);
                editWord.insert(i, alphabet[j]);
                possibleWords.add(editWord.toString());
            }
        }
    }
    //Deleting a single character
    public void delete(String inputWord, List<String> possibleWords) {
        for(int i = 0; i < inputWord.length(); i++) {
            StringBuilder editWord = new StringBuilder();
            editWord.append(inputWord);
            editWord.deleteCharAt(i);
            possibleWords.add(editWord.toString());
        }
    }
    //Altering a single character
    public void alter (String inputWord, List<String> possibleWords) {
        char[] alphabet = new char[26];
        for (int i = 0; i < 26; i++) {
            alphabet[i] = (char)(i+'a');
        }
        for(int i = 0; i < inputWord.length(); i++) {
            for (int j = 0; j < 26; j++) {
                StringBuilder editWord = new StringBuilder();
                editWord.append(inputWord);
                editWord.deleteCharAt(i);
                editWord.insert(i, alphabet[j]);
                possibleWords.add(editWord.toString());
            }
        }
    }
    //Switching the positions of two characters
    public void transpose (String inputWord, List<String> possibleWords) {
        for(int i = 0; i < inputWord.length(); i++) {
            for(int j = 0; j < inputWord.length(); j++) {
                StringBuilder editWord = new StringBuilder();
                editWord.append(inputWord);
                char swap1 = editWord.charAt(i);
                char swap2 = editWord.charAt(j);
                editWord.deleteCharAt(i);
                editWord.insert(i, swap2);
                editWord.deleteCharAt(i);
                editWord.insert(j, swap1);
                possibleWords.add(editWord.toString());
            }
        }
    }
}
