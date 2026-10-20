package ru.yandex.practicum;

import ru.yandex.practicum.wordle.WordleGame;
import ru.yandex.practicum.wordle.WordleGameException;
import ru.yandex.practicum.wordle.WordNotFoundInDictionary;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.Scanner;

public class Wordle {

    private static final String DICTIONARY_FILE = "words_ru.txt";
    private static final String LOG_FILE = "log.txt";


    public static void main(String[] args) throws IOException {
        try (PrintWriter logger = new PrintWriter(LOG_FILE, StandardCharsets.UTF_8)) {
            WordleDictionary dictionary = new WordleDictionaryLoader(logger).load(DICTIONARY_FILE);

            try {
                gameSession(dictionary);
            } catch (WordleGameException e) {
                System.out.println("Неожиданная проблема в игре: " + e.getMessage());
            }
        } catch (DictionaryLoadException e) {
            System.out.println("Не удалось загрузить словарь слов. Причина: " + e.getMessage());
        }

        System.out.println("Программа завершена.");
    }


    private static void gameSession(WordleDictionary dictionary) throws WordleGameException {
        WordleGame game = new WordleGame(dictionary, new Random());

        game.initSession();
        Scanner scanner = new Scanner(System.in);
        while (game.canPlay()) {
            System.out.println("Осталось попыток: " + game.getSteps());
            System.out.print("Введите слово: ");
            String userInput = scanner.nextLine();
            try {
                System.out.println(game.processInput(userInput));
            } catch (WordNotFoundInDictionary e) {
                System.out.println("Слово " + userInput + " не существует.");
            }
        }

        if (game.win()) {
            System.out.println("Вы выйграли!");
        } else {
            System.out.println("Было загадано слово: " + game.getAnswer());
        }
    }
}
