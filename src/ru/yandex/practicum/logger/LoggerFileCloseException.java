package ru.yandex.practicum.logger;

import java.io.Serial;

public class LoggerFileCloseException extends LoggerException {
    @Serial
    private static final long serialVersionUID = -6982997055869809635L;

    public LoggerFileCloseException(String message) {
        super(message);
    }
}
