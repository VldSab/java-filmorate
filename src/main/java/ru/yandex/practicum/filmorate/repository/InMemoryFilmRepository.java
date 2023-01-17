package ru.yandex.practicum.filmorate.repository;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class InMemoryFilmRepository {
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
}
