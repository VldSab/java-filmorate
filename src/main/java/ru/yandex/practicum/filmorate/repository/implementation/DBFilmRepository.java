package ru.yandex.practicum.filmorate.repository.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

@Repository("dbFilmRepository")
@RequiredArgsConstructor
public class DBFilmRepository implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film save(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("id");

        Long id = simpleJdbcInsert.executeAndReturnKey(film.toMap()).longValue();

        String sqlQueryGet = "SELECT id, name, description, release_date, duration " +
                             "FROM public.films " +
                             "WHERE id = ?";

        return jdbcTemplate.queryForObject(sqlQueryGet, this::mapRowToFilm, id);
    }

    @Override
    public Film update(Film film) {
        return null;
    }

    @Override
    public Collection<Film> list() {
        return null;
    }

    @Override
    public Optional<Film> findFilmById(Long id) {
        String sqlQueryGet = "SELECT id, name, description, release_date, duration " +
                "FROM public.films " +
                "WHERE id = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sqlQueryGet, this::mapRowToFilm, id));
    }

    @Override
    public boolean addLike(Long filmId, Long userId) {
        return false;
    }

    @Override
    public boolean deleteLike(Long filmId, Long userId) {
        return false;
    }

    @Override
    public Collection<Film> getMostPopularFilms(int count) {
        return null;
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNumber) throws SQLException {
        LocalDate releaseDate;
        try {
            releaseDate = resultSet.getDate("release_date").toLocalDate();
        } catch (NullPointerException e) {
            releaseDate = null;
        }
        return Film.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(releaseDate)
                .duration(resultSet.getLong("duration"))
                .build();
    }
}
