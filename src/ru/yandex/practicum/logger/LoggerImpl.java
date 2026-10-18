package ru.yandex.practicum.logger;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class LoggerImpl implements Logger, Closeable {

    private final BufferedWriter writer;

    public LoggerImpl(String fileName) throws LoggerFileOpenException {
        try {
            writer = new BufferedWriter(new FileWriter(fileName, true));
            writer.write("-".repeat(40) + "\n");
        } catch (IOException e) {
            throw new LoggerFileOpenException(fileName);
        }
    }


    @Override
    public void error(String message) throws LoggerIOException {
        try {
            LocalDateTime currentDateTime = LocalDateTime.now();
            writer.write("ERROR (" + currentDateTime + "): " + message + "\n");
        } catch (IOException e) {
            throw new LoggerIOException("Ошиба записи в журнал логов.");
        }
    }

    @Override
    public void close() throws LoggerFileCloseException {
        try {
            writer.close();
        } catch (IOException e) {
            throw new LoggerFileCloseException("Не удалось закрыть файл лога.");
        }
    }
}
