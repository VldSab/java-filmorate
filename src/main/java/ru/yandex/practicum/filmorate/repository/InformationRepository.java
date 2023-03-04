package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface InformationRepository {
    Mpa getMpaById(int id);
    Genre getGenreById(int id);
    List<Mpa> getAllMpas();
    List<Genre> getAllGenres();
}
