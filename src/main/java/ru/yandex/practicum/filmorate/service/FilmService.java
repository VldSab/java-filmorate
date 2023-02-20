package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmService {
    Film addFilm(Film film) throws ValidationException;
    Film updateFilm(Film film) throws ValidationException, NotFoundException;
    Film getFilm(Long id) throws NotFoundException;
    Collection<Film> listFilms();
    boolean addLike(Long filmId, Long userId) throws NotFoundException;
    boolean deleteLike(Long filmId, Long userId) throws NotFoundException;
    Collection<Film> getMostPopularFilms(int count);
}
