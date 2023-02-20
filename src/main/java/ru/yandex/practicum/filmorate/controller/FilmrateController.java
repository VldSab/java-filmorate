package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Response;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
public abstract class FilmrateController {
    public ResponseEntity<Response> createResponse(HttpStatus status, String message, Object data) {
        if (status.equals(HttpStatus.OK))
            return ResponseEntity.ok(
                    Response.builder()
                            .time(LocalDateTime.now())
                            .message(message)
                            .status(status)
                            .statusCode(status.value())
                            .data(data)
                            .build()
            );
        return ResponseEntity.badRequest().body(
                Response.builder()
                        .time(LocalDateTime.now())
                        .message(message)
                        .status(status)
                        .statusCode(status.value())
                        .data("")
                        .build()
        );
    }
    public ResponseEntity<?> createRawResponse(HttpStatus status, Object data) {
        if (status.equals(HttpStatus.OK))
            return ResponseEntity.ok(data);
        if (status.equals(HttpStatus.BAD_REQUEST))
            return ResponseEntity.badRequest().body(
                    Response.builder()
                            .time(LocalDateTime.now())
                            .status(status)
                            .statusCode(status.value())
                            .build()
            );
        if (status.equals(HttpStatus.NOT_FOUND))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Response.builder()
                            .time(LocalDateTime.now())
                            .status(status)
                            .statusCode(status.value())
                            .build()
            );
        return ResponseEntity.internalServerError().build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleHappinessOverflow(final NullPointerException e) {
        return Map.of(
                "error", "Не передан параметр",
                "message", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleHappinessOverflow(final ValidationException e) {
        return Map.of(
                "error", "Ошибка валидации",
                "message", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleHappinessOverflow(final NotFoundException e) {
        return Map.of(
                "error", "Ресурс не найден",
                "message", e.getMessage()
        );
    }
}
