package ru.yandex.practicum.wordle;

import java.io.Serial;

public class WordInvalidFormat extends WordleGameException {
    @Serial
    private static final long serialVersionUID = 2793546620186405213L;

    public WordInvalidFormat(String message) {
        super(message);
    }
}
