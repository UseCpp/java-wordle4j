package ru.yandex.practicum;

import ru.yandex.practicum.logger.LoggerImpl;
import ru.yandex.practicum.logger.LoggerException;
import ru.yandex.practicum.wordle.WordleGame;
import ru.yandex.practicum.wordle.WordleGameException;
import ru.yandex.practicum.wordle.WordNotFoundInDictionary;

import java.util.Random;
import java.util.Scanner;

public class Wordle {

    private static final String DICTIONARY_FILE = "words_ru.txt";
    private static final String LOG_FILE = "log.txt";


    public static void main(String[] args) {
        try(LoggerImpl logger = new LoggerImpl(LOG_FILE)) {
            WordleDictionary dictionary = new WordleDictionaryLoader(logger).load(DICTIONARY_FILE);
            if (dictionary.size() == 0) {
                System.out.println("Не удалось загрузить базу данных слов.");
            }
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
                } catch (WordleGameException e) {
                    System.out.println("Ошибка в игре: " + e.getMessage());
                }
            }

            if (game.win()) {
                System.out.println("Вы выйграли!");
            } else {
                System.out.println("Было загадано слово: " + game.getAnswer());
            }

        } catch (LoggerException e) {
            System.out.println("Ошибка при работе с журналом логов. Причина: " + e.getMessage());
        }

        System.out.println("Программа завершена.");
    }
}
