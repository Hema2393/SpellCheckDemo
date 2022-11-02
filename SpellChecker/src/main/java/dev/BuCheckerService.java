package dev;

import domain.*;
import dev.Levenshtein;
import dao.*;
import java.util.ArrayDeque;

/**
 * All the program functions are called through this class.
 *
 * @author sallasal
 */
public class BuCheckerService {

    private DictionaryDao initDictionary;
    private String[] dictionary;
    private Levenshtein levenshtein;
    private OptimalStringAlignment optimalStringAlignment;

    /**
     * Creates CheckerService instance and initializes the dictionary based on
     * the resource file
     */
    public BuCheckerService() {
        initDictionary = new FileDictionaryDao();
        this.dictionary = null;
        this.levenshtein = new Levenshtein();
        this.optimalStringAlignment = new OptimalStringAlignment();

        try {
            this.dictionary = initDictionary.initializeDictionary();
        } catch (Exception ex) {
            System.out.println("Initializing the dictionary did not succeed. Program is halted.");
        }
    }

    /**
     * Checks if the input word is found from dictionary
     *
     * @param input the user input
     * @return true, if the input matches to word in dictionary, false otherwise
     */
    public boolean checkWordFromDictionary(String input) {
        for (String word : this.dictionary) {
            if (input.equals(word)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Calculates optimal string alignment distance
     *
     * @param word1 First word for OSA distance
     * @param word2 Second word for OSA distance
     * @return optimal string alignment distance as integer
     */
    public double getOptimalStringAlignmentDistance(String word1, String word2) {
        return this.optimalStringAlignment.weightedOSA(word1, word2);
    }

    /**
     * Creates 10 best suggestions based on optimal string alignment distance
     *
     * @param wordFor the false word the distance is compared to
     * @return 10 best suggestions as String array
     */
    public String[] getSuggestions(String wordFor) {
        ArrayDeqImplementation distance1 = new ArrayDeqImplementation();
        ArrayDeqImplementation distance2 = new ArrayDeqImplementation();

        String[] suggestions = new String[10];
        for (int i = 0; i < 10; i++) {
            suggestions[i] = "-";
        }

        for (String word : dictionary) {
            double distance = this.getOptimalStringAlignmentDistance(wordFor, word);
            
            if (distance <= 1) {
                distance1.addLast(word);
            } else if (distance > 1 && distance <= 2) {
                distance2.addLast(word);
            }
        }

        int i = 0;

        while (i < 10 && distance1.size() > 0) {
            suggestions[i] = distance1.remove();
            i++;
        }

        while (i < 10 && distance2.size() > 0) {
            suggestions[i] = distance2.remove();
        }

        return suggestions;
    }

    /**
     * Splits input to word array, sanitizes the input.
     *
     * @param input Input string for splitting
     * @return String array that contains single words
     */
    public String[] getWords(String input) {

        input = input.toLowerCase().replaceAll("\\?|\\t|\\n|\"|\'|[.,!:;-]", " ");

        String[] words = input.split(" ");

        return words;
    }

    /**
     * Calculates Levenshtein edit distance (basic one). Dev method, removed
     * from final version.
     *
     * @param word1 First word for Levenshtein edit distance
     * @param word2 Second word for Levenshtein edit distance
     * @return Levenshtein edit distance as integer
     */
    public int devGetLevenshteinDistance(String word1, String word2) {
        return this.levenshtein.levenshteinDistance(word1, word2);
    }

}
