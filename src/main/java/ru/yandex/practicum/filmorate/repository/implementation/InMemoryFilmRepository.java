package ru.yandex.practicum.filmorate.repository.implementation;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class InMemoryFilmRepository implements FilmStorage {
    /**
     * Хранилище фильмов в оперативной памяти.
     */
    private final Map<Long, Film> filmsStorage = new HashMap<>();
    private static Long id = 1L;

    public Film save(Film film) {
        film.setId(id);
        filmsStorage.put(id, film);
        id += 1;
        return film;
    }

    public Film update(Film film) {
        Film filmToUpdate = filmsStorage.get(film.getId());
        if (filmToUpdate == null) {
            save(film);
            return film;
        }
        filmToUpdate.setName(film.getName());
        filmToUpdate.setDescription(film.getDescription());
        filmToUpdate.setReleaseDate(film.getReleaseDate());
        filmToUpdate.setDuration(film.getDuration());
        return filmToUpdate;
    }

    public Collection<Film> list() {
        return filmsStorage.values();
    }

    public Optional<Film> findFilmById(Long id) {
        return filmsStorage.containsKey(id)
                ? Optional.of(filmsStorage.get(id))
                : Optional.empty();
    }

    @Override
    public boolean addLike(Long filmId, Long userId) {
        Film film = filmsStorage.get(filmId);
        return film.getLikes().add(userId);
    }

    @Override
    public boolean deleteLike(Long filmId, Long userId) {
        Film film = filmsStorage.get(filmId);
        return film.getLikes().remove(userId);
    }

    @Override
    public Collection<Film> getMostPopularFilms(final int count) {
        return filmsStorage.values().stream()
                .sorted(Comparator.comparingInt(it -> -it.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }

}
