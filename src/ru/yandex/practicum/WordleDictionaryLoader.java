package ru.yandex.practicum;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class WordleDictionaryLoader {

    private final PrintWriter logger;

    public WordleDictionaryLoader(PrintWriter logger) {
        this.logger = logger;
    }

    WordleDictionary load(String fileName) throws DictionaryLoadException {
        WordleDictionary result = new WordleDictionary();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName, StandardCharsets.UTF_8))) {
            while (reader.ready()) {
                result.add(reader.readLine());
            }
        } catch (FileNotFoundException e) {
            String message = "Не удалось найти файл словаря по имени: " + fileName;
            logger.println(message);
            e.printStackTrace(logger);
            throw new DictionaryLoadException(message);
        } catch (IOException e) {
            String message = "Не удалось взаимодейстовать с файлом словаря по имени: " + fileName;
            logger.println(message);
            e.printStackTrace(logger);
            throw new DictionaryLoadException(message);
        }
        return result;
    }
}
