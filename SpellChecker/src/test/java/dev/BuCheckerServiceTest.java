package dev;

import dev.*;
import static org.hamcrest.CoreMatchers.not;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test CheckerService functions
 *
 * @author sallasal
 */
public class BuCheckerServiceTest {

    BuCheckerService checkerService;

    @Before
    public void setUp() {
        this.checkerService = new BuCheckerService();
    }

    @Test
    public void wordIsFoundFromDictionary() {
        assertTrue(checkerService.checkWordFromDictionary("dictionary"));
    }

    @Test
    public void wordIsNotFoundFromDictionary() {
        assertFalse(checkerService.checkWordFromDictionary("dictonay"));
    }
    
    @Test
    public void levenshteinDistanceCallWorks() {
        int distance = checkerService.devGetLevenshteinDistance("unicorn", "uincon");
        assertEquals(3, distance);
    }
    
    @Test
    public void osaDistanceCallWorks() {
        double distance = checkerService.getOptimalStringAlignmentDistance("unicorn", "uincon");
        assertEquals(1.362, distance, 0.001);
    }
    
    @Test
    public void getSuggestionsOffersLessThan10Correctly() {
        String[] suggestions = checkerService.getSuggestions("marvelous");
        assertEquals("marvellous", suggestions[0]);
        assertEquals("-", suggestions[2]);
    }
    
    @Test
    public void getSuggestionsOffersBest10Correctly() {
        String[] suggestions = checkerService.getSuggestions("lat");
        assertThat(suggestions[0], not("-"));
        assertThat(suggestions[3], not("-"));
        assertThat(suggestions[9], not("-"));
    }
    
    @Test
    public void getWordsReturnsCorrectAmountOfWords() {
        String input = "This is the-test.input?that should return ten words.";
        String[] words = checkerService.getWords(input);
        assertEquals(10, words.length);
    }
}