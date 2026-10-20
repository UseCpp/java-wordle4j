package ru.yandex.practicum;

import java.io.IOException;
import java.io.Serial;

public class DictionaryLoadException extends IOException {
    @Serial
    private static final long serialVersionUID = 3372248466584303594L;

    public DictionaryLoadException(String message) {
        super(message);
    }
}
