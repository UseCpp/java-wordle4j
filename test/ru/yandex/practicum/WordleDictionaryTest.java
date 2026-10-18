package ru.yandex.practicum;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class WordleDictionaryTest {
    @Test
    void add_validFiveLetterWord_returnsTrueAndIncreasesSize() {
        WordleDictionary dict = new WordleDictionary();
        Assertions.assertTrue(dict.add("слово"));
        Assertions.assertEquals(1, dict.size());
    }

    @Test
    void add_uppercaseWord_isNormalizedToLowercase() {
        WordleDictionary dict = new WordleDictionary();
        dict.add("СЛОВО");
        Assertions.assertTrue(dict.contains("слово"));
    }

    @Test
    void add_wordWithSpacesAround_isTrimmed() {
        WordleDictionary dict = new WordleDictionary();
        dict.add("  слово  ");
        Assertions.assertTrue(dict.contains("слово"));
    }

    @Test
    void add_tooShortWord_returnsFalse() {
        WordleDictionary dict = new WordleDictionary();
        Assertions.assertFalse(dict.add("кот"));
        Assertions.assertEquals(0, dict.size());
    }

    @Test
    void normalize_trimsLowercasesAndReplacesYo() {
        Assertions.assertEquals("ежики", WordleDictionary.normalize("  ЁЖИКИ  "));
    }

    @Test
    void normalize_doesNotChangeAlreadyNormalized() {
        Assertions.assertEquals("слово", WordleDictionary.normalize("слово"));
    }

    @Test
    void normalize_emptyString_returnsEmpty() {
        Assertions.assertTrue(WordleDictionary.normalize("").isEmpty());
    }
}
