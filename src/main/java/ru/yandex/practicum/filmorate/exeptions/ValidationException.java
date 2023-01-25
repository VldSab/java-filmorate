package ru.yandex.practicum.filmorate.exeptions;

public class ValidationException extends Exception {
    /**
     * Ошибка валидации.
     */
    public ValidationException() {
    }

    public ValidationException(String message) {
        super(message);
    }
}
