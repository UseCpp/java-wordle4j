package ru.yandex.practicum.wordle;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Всё, что игрок знает о загаданном слове по уже полученным маскам.
 * Класс намеренно не знает ответа — подсказки строятся только на открытой информации.
 */
public class WordleUserKnownInformation {

    private static final char UNKNOWN = '\0';

    private final char[] exactChars;
    private final Map<Character, Set<Integer>> forbiddenPositions = new HashMap<>();
    private final Map<Character, Integer> minCounts = new HashMap<>();
    private final Map<Character, Integer> maxCounts = new HashMap<>();

    public WordleUserKnownInformation(int wordLength) {
        exactChars = new char[wordLength];
    }

    public void analyze(String userInput, String mask) {
        if (userInput.length() != exactChars.length || mask.length() != exactChars.length) {
            throw new IllegalArgumentException("Длина слова и маски не совпадает: " + userInput + " / " + mask);
        }
        Map<Character, Integer> foundCounts = new HashMap<>();
        Set<Character> hasAbsent = new HashSet<>();
        for (int i = 0; i < mask.length(); i++) {
            char smb = userInput.charAt(i);
            switch (mask.charAt(i)) {
                case WordleGame.MASK_EXACT -> {
                    exactChars[i] = smb;
                    foundCounts.merge(smb, 1, Integer::sum);
                }
                case WordleGame.MASK_PRESENT -> {
                    forbiddenPositions.computeIfAbsent(smb, k -> new HashSet<>()).add(i);
                    foundCounts.merge(smb, 1, Integer::sum);
                }
                case WordleGame.MASK_ABSENT -> {
                    forbiddenPositions.computeIfAbsent(smb, k -> new HashSet<>()).add(i);
                    hasAbsent.add(smb);
                }
                default -> throw new IllegalArgumentException("Неизвестный символ маски: " + mask);
            }
        }
        // Найденные (+ и ^) вхождения дают нижнюю границу количества буквы в ответе,
        // а лишнее вхождение с '-' — точное количество.
        foundCounts.forEach((smb, count) -> minCounts.merge(smb, count, Math::max));
        for (char smb : hasAbsent) {
            maxCounts.merge(smb, foundCounts.getOrDefault(smb, 0), Math::min);
        }
    }

    public boolean matches(String word) {
        for (int i = 0; i < exactChars.length; i++) {
            if (exactChars[i] != UNKNOWN && exactChars[i] != word.charAt(i)) {
                return false;
            }
        }
        for (var entry : forbiddenPositions.entrySet()) {
            for (int position : entry.getValue()) {
                if (word.charAt(position) == entry.getKey()) {
                    return false;
                }
            }
        }
        for (var entry : minCounts.entrySet()) {
            if (countOf(word, entry.getKey()) < entry.getValue()) {
                return false;
            }
        }
        for (var entry : maxCounts.entrySet()) {
            if (countOf(word, entry.getKey()) > entry.getValue()) {
                return false;
            }
        }
        return true;
    }

    private static int countOf(String word, char smb) {
        int count = 0;
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == smb) {
                count++;
            }
        }
        return count;
    }
}
