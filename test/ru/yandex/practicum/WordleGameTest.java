package ru.yandex.practicum;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.wordle.WordNotFoundInDictionary;
import ru.yandex.practicum.wordle.WordleGame;
import ru.yandex.practicum.wordle.WordleGameException;
import ru.yandex.practicum.wordle.WordleUnexpectedInput;

import java.io.Serial;
import java.util.Random;
import java.util.stream.Stream;

public class WordleGameTest {

    private static WordleDictionary dictionaryOf(String... words) {
        WordleDictionary d = new WordleDictionary();
        Stream.of(words).forEach(d::add);
        return d;
    }

    @Test
    public void when_rightInput_thenWins() {
        WordleGame game = new WordleGame(dictionaryOf("слово"), new Random());
        try {
            game.initSession();
            game.processInput("слово");
        } catch (WordleGameException ignored) {
        }
        Assertions.assertTrue(game.win());
        Assertions.assertFalse(game.canPlay());
    }

    @Test
    public void when_noExistWordInput_thenException() {
        WordleGame game = new WordleGame(dictionaryOf("слово"), new Random());
        game.initSession();
        Assertions.assertThrows(WordNotFoundInDictionary.class, () -> game.processInput("что-то"));
    }

    @Test
    public void when_autoInput_thenWins() throws WordleGameException {
        WordleGame game = new WordleGame(dictionaryOf("арбуз", "аисты", "волна", "город", "весна"), new Random());
        game.initSession();
        while (game.canPlay()) {
            game.processInput("");
        }
        Assertions.assertTrue(game.win());
    }

    @Test
    public void test_InputMasks() throws WordleGameException {
        WordleGame game = new WordleGame(dictionaryOf("арбуз", "ааааа", "арааа", "аяяяя"), new Random() {
            @Serial
            private static final long serialVersionUID = -8933390458279765565L;

            @Override
            public int nextInt(int bound) {
                return 0;
            }
        });
        game.initSession();
        var output = game.processInput("аяяяя");
        Assertions.assertEquals("+----", output.mask());
        output = game.processInput("ааааа");
        Assertions.assertEquals("+^^^^", output.mask());

        output = game.processInput("арааа");
        Assertions.assertEquals("++^^^", output.mask());
        output = game.processInput("арбуз");
        Assertions.assertEquals("+++++", output.mask());
    }

    @Test
    public void testGameObjectCondition() {
        WordleGame game = new WordleGame(dictionaryOf("слово"), new Random());
        Assertions.assertFalse(game.canPlay());
        game.initSession();
        Assertions.assertTrue(game.canPlay());
    }

    @Test
    public void when_gameIsNotInit_andInput_thenException() {
        WordleGame game = new WordleGame(dictionaryOf("слово"), new Random());
        Assertions.assertThrows(WordleUnexpectedInput.class, () -> game.processInput(""));
    }

}
