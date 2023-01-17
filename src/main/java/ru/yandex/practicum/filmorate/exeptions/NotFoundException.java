package ru.yandex.practicum.filmorate.exeptions;

public class NotFoundException extends Exception {
    public NotFoundException() {
    }

    public NotFoundException(String message) {
        super(message);
    }
}
