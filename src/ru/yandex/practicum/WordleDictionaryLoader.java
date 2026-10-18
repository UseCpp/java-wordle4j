package ru.yandex.practicum;

import ru.yandex.practicum.logger.Logger;
import ru.yandex.practicum.logger.LoggerIOException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class WordleDictionaryLoader {

    private final Logger logger;

    public WordleDictionaryLoader(Logger logger) {
        this.logger = logger;
    }

    WordleDictionary load(String fileName) throws LoggerIOException {
        WordleDictionary result = new WordleDictionary();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName, StandardCharsets.UTF_8))) {
            while (reader.ready()) {
                result.add(reader.readLine());
            }
        } catch (FileNotFoundException e) {
            logger.error("Не удалось найти файл словая по имени: " + fileName);
        } catch (IOException e) {
            logger.error("Не удалось взаимодейстовать с файлом словаря по имени: " + fileName);
        }
        return result;
    }
}
