package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Response;
import ru.yandex.practicum.filmorate.service.implementation.FilmServiceStandard;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Map;

@Controller
@RequestMapping("/films")
@RequiredArgsConstructor
@Slf4j
public class FilmController {
    /**
     * Обработка ззапросов для работы с фильмами.
     */
    private final FilmServiceStandard filmService;
    @PostMapping
    public ResponseEntity<Response> saveFilm(@Valid @RequestBody Film film) {
        Film savedFilm;
        String message;
        HttpStatus status;
        try {
            savedFilm = filmService.addFilm(film);
            message = "Film added";
            status = HttpStatus.OK;
            log.info("Film added with id {}", film.getId());
        } catch (ValidationException e) {
            savedFilm = null;
            message = e.getMessage();
            status = HttpStatus.BAD_REQUEST;
            log.info("Film not added with exception: {}", e.getMessage());
        }
        return getResponseEntity(savedFilm, message, status);
    }



    @PutMapping
    public ResponseEntity<Response> updateFilm(@Valid @RequestBody Film film) {
        Film uppdatedFilm;
        String message;
        HttpStatus status;
        try {
            uppdatedFilm = filmService.updateFilm(film);
            message = "Film updated";
            status = HttpStatus.OK;
            log.info("Film updated with id {}", film.getId());
        } catch (ValidationException e) {
            uppdatedFilm = null;
            message = e.getMessage();
            status = HttpStatus.BAD_REQUEST;
            log.info("Film not updated with exception: {}", e.getMessage());
        }
        return getResponseEntity(uppdatedFilm, message, status);
    }

    @GetMapping
    public ResponseEntity<Response> getAllFilms() {
        log.info("Getting all films list");
        return ResponseEntity.ok(
                Response.builder()
                        .time(LocalDateTime.now())
                        .message("All films")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .data(Map.of("films", filmService.listFilms()))
                        .build()
        );
    }

    private ResponseEntity<Response> getResponseEntity(Film film, String message, HttpStatus status) {
        if (status.equals(HttpStatus.OK))
            return ResponseEntity.ok(
                    Response.builder()
                            .time(LocalDateTime.now())
                            .message(message)
                            .status(status)
                            .statusCode(status.value())
                            .data(Map.of("film", film))
                            .build()
            );
        return ResponseEntity.badRequest().body(
                Response.builder()
                        .time(LocalDateTime.now())
                        .message(message)
                        .status(status)
                        .statusCode(status.value())
                        .data(Map.of("film", ""))
                        .build()
        );
    }
}
