package ru.yandex.practicum.logger;

import java.io.Serial;

public class LoggerFileOpenException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -7500197250514207280L;

    public LoggerFileOpenException(String fileName) {
        super(fileName);
    }

    @Override
    public String getMessage() {
        return "Не удалось открыть файл по имени " + super.getMessage();
    }
}
