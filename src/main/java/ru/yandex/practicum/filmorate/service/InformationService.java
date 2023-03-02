package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface InformationService {
    Mpa getMpa(int id) throws NotFoundException;
    Genre getGenre(int id) throws NotFoundException;
    List<Mpa> listMpa();
    List<Genre> listGenre();
}
