package ru.yandex.practicum.wordle;

import java.io.Serial;

public class WordleUnexpectedInput extends WordleGameException {
    @Serial
    private static final long serialVersionUID = -1601280111010008387L;

    public WordleUnexpectedInput(String message) {
        super(message);
    }
}
