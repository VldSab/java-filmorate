package ru.yandex.practicum.filmorate.controller;

import netscape.javascript.JSObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.model.Response;

import java.time.LocalDateTime;
import java.util.Optional;

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
            return ResponseEntity.of(Optional.of(
                    Response.builder()
                            .time(LocalDateTime.now())
                            .status(status)
                            .statusCode(status.value())
                            .build()
            ));
        return ResponseEntity.internalServerError().build();
    }
}
