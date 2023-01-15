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
    @PostMapping("/save")
    public ResponseEntity<Response> saveFilm(@Valid @RequestBody Film film) {
        Film savedFilm;
        String message;
        try {
            savedFilm = filmService.addFilm(film);
            message = "Film added";
            log.info("Film added with id {}", film.getId());
        } catch (ValidationException e) {
            savedFilm = null;
            message = e.getMessage();
            log.info("Film not added with exception: {}", e.getMessage());
        }
        return ResponseEntity.ok(
                Response.builder()
                        .time(LocalDateTime.now())
                        .message(message)
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .data(Map.of("film", savedFilm == null ? "" : savedFilm))
                        .build()
        );
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Response> updateFilm(@Valid @RequestBody Film film, @PathVariable("id") Long id) {

        Film uppdatedFilm;
        String message;
        try {
            uppdatedFilm = filmService.updateFilm(film, id);
            message = "Film updated";
            log.info("Film updated with id {}", film.getId());
        } catch (ValidationException e) {
            uppdatedFilm = null;
            message = e.getMessage();
            log.info("Film not updated with exception: {}", e.getMessage());
        }

        return ResponseEntity.ok(
                Response.builder()
                        .time(LocalDateTime.now())
                        .message(message)
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .data(Map.of("film", uppdatedFilm == null ? "" : uppdatedFilm))
                        .build()
        );
    }

    @GetMapping("/list")
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
}
