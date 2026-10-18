package ru.yandex.practicum.wordle;

import java.io.Serial;

public class WordNotFoundInDictionary extends WordleGameException {
    @Serial
    private static final long serialVersionUID = 4155855495937906544L;

    public WordNotFoundInDictionary(String message) {
        super(message);
    }
}
