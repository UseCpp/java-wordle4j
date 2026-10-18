package ru.yandex.practicum.logger;

import java.io.IOException;
import java.io.Serial;

public class LoggerException extends IOException {
    @Serial
    private static final long serialVersionUID = -6437885507740937656L;

    public LoggerException(String message) {
        super(message);
    }
}
