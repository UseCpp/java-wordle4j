package ru.yandex.practicum.logger;

import java.io.Serial;

public class LoggerIOException extends LoggerException {
    @Serial
    private static final long serialVersionUID = -3620922134142584076L;

    public LoggerIOException(String message) {
        super(message);
    }
}
