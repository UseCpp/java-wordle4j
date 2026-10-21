package ru.yandex.practicum.wordle;

import java.io.Serial;

public class WordleGameException extends Exception {
    @Serial
    private static final long serialVersionUID = 4633039447375622607L;

    public WordleGameException(String message) {
        super(message);
    }
}
