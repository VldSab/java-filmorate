package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {
    Film save(Film film);
    Film update(Film film);
    Collection<Film> list();
    Optional<Film> findFilmById(Long id);
    boolean addLike(Long filmId, Long userId);
    boolean deleteLike(Long filmId, Long userId);
    Collection<Film> getMostPopularFilms(int count);

}
