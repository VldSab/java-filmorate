package ru.yandex.practicum.filmorate.controller.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.FilmrateController;
import ru.yandex.practicum.filmorate.exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Response;
import ru.yandex.practicum.filmorate.service.implementation.FilmServiceStandard;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/films")
@RequiredArgsConstructor
@Slf4j
public class FilmController extends FilmrateController {
    /**
     * Обработка ззапросов для работы с фильмами.
     */
    private final FilmServiceStandard filmService;
    @PostMapping
    public ResponseEntity<?> saveFilm(@Valid @RequestBody Film film) {
        Film savedFilm;
        HttpStatus status;
        try {
            savedFilm = filmService.addFilm(film);
            status = HttpStatus.OK;
            log.info("Film added with id {}", film.getId());
        } catch (ValidationException e) {
            savedFilm = null;
            status = HttpStatus.BAD_REQUEST;
            log.info("Film not added with exception: {}", e.getMessage());
        }
        return createRawResponse(status, savedFilm);
    }

    @PutMapping
    public ResponseEntity<?> updateFilm(@Valid @RequestBody Film film) {
        Film uppdatedFilm;
        HttpStatus status;
        try {
            uppdatedFilm = filmService.updateFilm(film);
            status = HttpStatus.OK;
            log.info("Film updated with id {}", film.getId());
        } catch (ValidationException e) {
            uppdatedFilm = null;
            status = HttpStatus.BAD_REQUEST;
            log.info("Film not updated with exception: {}", e.getMessage());
        } catch (NotFoundException e) {
            uppdatedFilm = null;
            status = HttpStatus.NOT_FOUND;
        }
        return createRawResponse(status, uppdatedFilm);
    }

    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<?> like(@PathVariable("id") Long filmId, @PathVariable Long userId) throws NotFoundException {
        if (filmId == null || userId == null)
            throw new NullPointerException("Не указан id фильма или id пользователя");
        return createRawResponse(HttpStatus.OK, filmService.addLike(filmId, userId));
    }

    @DeleteMapping("/{id}/like/{userId}")
    public ResponseEntity<?> deleteLike(@PathVariable("id") Long filmId, @PathVariable Long userId) throws NotFoundException {
        if (filmId == null || userId == null)
            throw new NullPointerException("Не указан id фильма или id пользователя");
        return createRawResponse(HttpStatus.OK, filmService.deleteLike(filmId, userId));
    }

    @GetMapping
    public ResponseEntity<?> getAllFilms() {
        log.info("Getting all films list");
        return createRawResponse(HttpStatus.OK, filmService.listFilms());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFilm(@PathVariable Long id) throws NotFoundException {
        if (id == null)
            throw new NullPointerException("Не передан id пользователя");
        return createRawResponse(HttpStatus.OK, filmService.getFilm(id));
    }

    @GetMapping("/popular")
    public ResponseEntity<?> getMostPopular(@RequestParam(required = false) Integer count) {
        final int DEFAULT_COUNT = 10;
        if (count == null) count = DEFAULT_COUNT;
        return createRawResponse(HttpStatus.OK, filmService.getMostPopularFilms(count));
    }

}
