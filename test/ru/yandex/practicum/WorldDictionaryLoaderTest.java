package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WorldDictionaryLoaderTest {

    @TempDir
    Path tempDir;

    private WordleDictionaryLoader loader;

    @BeforeEach
    void setUp() {
        OutputStream stream = new OutputStream() {
            @Override
            public void write(int b) {}
        };
        loader = new WordleDictionaryLoader(new PrintWriter(stream));
    }

    private Path writeFile(String name, String content) throws IOException {
        Path file = tempDir.resolve(name);
        Files.writeString(file, content, StandardCharsets.UTF_8);
        return file;
    }

    @Test
    void load_validFileWithFiveLetterWords_returnsDictionaryWithAllWords() throws Exception {
        Path file = writeFile("words.txt", "слово\nдомик\nкнига\n");

        WordleDictionary dict = loader.load(file.toString());

        assertEquals(3, dict.size());
        assertTrue(dict.contains("слово"));
        assertTrue(dict.contains("домик"));
        assertTrue(dict.contains("книга"));
    }

    @Test
    void load_lonWords_returnsEmptyDictionary() throws Exception {
        Path file = writeFile("words.txt", "первый\nвторой\nтретий\n");

        WordleDictionary dict = loader.load(file.toString());

        assertEquals(0, dict.size());
    }

    @Test
    void load_emptyFile_returnsEmptyDictionary() throws Exception {
        Path file = writeFile("empty.txt", "");

        WordleDictionary dict = loader.load(file.toString());

        assertNotNull(dict);
        assertEquals(0, dict.size());
    }

}
