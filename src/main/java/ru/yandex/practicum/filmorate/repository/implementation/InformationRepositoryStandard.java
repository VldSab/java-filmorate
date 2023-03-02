package ru.yandex.practicum.filmorate.repository.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.constants.GenreNames;
import ru.yandex.practicum.filmorate.model.constants.MpaNames;
import ru.yandex.practicum.filmorate.repository.InformationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class InformationRepositoryStandard implements InformationRepository {
    @Override
    public Mpa getMpaById(int id) {
        return new Mpa(id, MpaNames.getNameById(id));
    }

    @Override
    public Genre getGenreById(int id) {
        return new Genre(id, GenreNames.getNameById(id));
    }

    @Override
    public List<Mpa> getAllMpas() {
        return MpaNames.getAllNames().entrySet().stream()
                .map(it -> new Mpa(it.getKey(), it.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Genre> getAllGenres() {
        return GenreNames.getAllNames().entrySet().stream()
                .map(it -> new Genre(it.getKey(), it.getValue()))
                .collect(Collectors.toList());
    }
}
