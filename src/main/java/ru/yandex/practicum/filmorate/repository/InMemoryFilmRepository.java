package ru.yandex.practicum.filmorate.repository;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class InMemoryFilmRepository {
    /**
     * Хранилище фильмов в оперативной памяти.
     */
    private final Map<Long, Film> filmsStorage = new HashMap();

    public Film save(Film film) {
        filmsStorage.put(film.getId(), film);
        return film;
    }

    public Film update(Film film, Long id) {
        Film filmToUpdate = filmsStorage.get(id);
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
}
