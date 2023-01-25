package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmService {
    Film addFilm(Film film) throws ValidationException;
    Film updateFilm(Film film) throws ValidationException, NotFoundException;
    Collection<Film> listFilms();
}
