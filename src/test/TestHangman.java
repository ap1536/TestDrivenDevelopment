package test;

import main.Hangman;
import org.junit.jupiter.api.*;

import java.lang.reflect.Executable;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class TestHangman {
    static Random random;
    int requestedLength;
    static Hangman hangman;

    @BeforeAll
    public static void setupCall(){
        random = new Random();
        hangman = new Hangman();
        hangman.loadWords();
    }
    @BeforeEach
    public  void setupTest(){
        requestedLength = random.nextInt(6)+5;
        hangman.score = 0;
    }

    @AfterEach
    public void tearDownTest(){
        //not needed in this test class
    }

    @AfterAll
    public static void tearDownClass(){
        //not needed in this test class
    }

    @Test
    void test_alphabetCountInAWord(){
        String word = "Pizza";
        char alphabet = 'a';
        int count = hangman.countAlphabet(word, alphabet);
        assertEquals(1, count);
    }

    @Test
    void test_lengthofFetchedWordRandom(){
        String word = hangman.fetchWord(requestedLength);
        assertTrue(requestedLength == word.length());
    }

    @Test
    void test_uniquenessOfFetchedWord(){
        Set<String> usedWordsSet = new HashSet<>();
        int round = 0;
        String word = null;
        while (round < 10){
            requestedLength = random.nextInt(6)+5;
            word = hangman.fetchWord(requestedLength);
            round++;
            assertTrue(usedWordsSet.add(word));
        }
    }

    @Test
    void test_fetchClueBeforeAnyGuess(){
        String clue = hangman.fetchClue("Pizza");
        assertEquals("-----", clue);
    }

    @Test
    void test_fetchClueAfterCorrectGuess(){
        String clue = hangman.fetchClue("Pizza");
        String newClue = hangman.fetchClue("pizza", clue, 'a');
        assertEquals("----a", newClue);
    }

    @Test
    void test_fetchClueAfterIncorrectGuess(){
        String clue = hangman.fetchClue("Pizza");
        String newClue = hangman.fetchClue("pizza", clue, 'x');
        assertEquals("-----", newClue);
    }

    @Test
    void test_whenInvalidGuessThenFetchClueThrowsException(){
        assertThrows(IllegalArgumentException.class,
                ()->hangman.fetchClue("Pizza", "-----", '1'));
    }

    @Test
    void test_whenInvalidGuessThenFetchClueThrowsExceptionWithMessage(){
        Exception e = assertThrows(IllegalArgumentException.class,
                ()->hangman.fetchClue("Pizza", "-----", '1'));
        assertEquals("Invalid Characters", e.getMessage());
    }

    @Test
    void test_remainingTrailsBeforeAnyGuess(){
        hangman.fetchWord(requestedLength);
        assertEquals(Hangman.MAX_TRAILS, hangman.remainingTrials);
    }

    @Test
    void test_remainingTrailsAfterOneGuess(){
        hangman.fetchWord(requestedLength);
        hangman.fetchClue("pizza", "-----", 'a');
        assertEquals(Hangman.MAX_TRAILS - 1, hangman.remainingTrials);
    }

    @Test
    void test_scoreBeforeAnyGuess(){
        hangman.fetchWord(requestedLength);
        assertEquals(0, hangman.score);
    }

    @Test
    void test_scoreAfterCorrectGuess(){
        String word = "pizza";
        String clue = "-----";
        char guess = 'a';

        hangman.fetchClue(word, clue, guess);
        assertEquals((double)Hangman.MAX_TRAILS/word.length(), hangman.score);
    }

    @Test
    void test_scoreAfterIncorrectGuess(){
        String word = "pizza";
        String clue = "-----";
        char guess = 'x';

        hangman.fetchClue(word, clue, guess);
        assertEquals(0, hangman.score);
    }
}
