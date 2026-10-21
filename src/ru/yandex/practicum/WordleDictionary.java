package ru.yandex.practicum;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class WordleDictionary {

    public static final int WORD_LENGTH = 5;

    private final List<String> words = new ArrayList<>();

    public boolean add(String word) {
        word = normalize(word);
        if (word.length() == WORD_LENGTH) {
            return words.add(word);
        }
        return false;
    }

    public int size() {
        return words.size();
    }

    public String get(int index) {
        return words.get(index);
    }

    public boolean contains(String word) {
        return words.contains(normalize(word));
    }

    public Stream<String> stream() {
        return words.stream();
    }

    public static String normalize(String word) {
        return word.trim().toLowerCase().replaceAll("ё", "е");
    }
}
