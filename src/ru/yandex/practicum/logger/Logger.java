package ru.yandex.practicum.logger;

public interface Logger {
    void error(String message) throws LoggerIOException;
}
