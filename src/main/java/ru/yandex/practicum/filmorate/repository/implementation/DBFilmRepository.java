package ru.yandex.practicum.filmorate.repository.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.enumerations.Mpa;
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

        String sqlQueryGet = "SELECT id, name, description, release_date, duration, mpa " +
                "FROM public.films " +
                "WHERE id = ?";

        return jdbcTemplate.queryForObject(sqlQueryGet, this::mapRowToFilm, id);
    }

    @Override
    public Film update(Film film) {
        String sqlUpdate = "UPDATE public.films " +
                "SET name = ?, description = ?, release_date = ?, duration = ?, mpa = ? " +
                "WHERE id = ?";
        jdbcTemplate.update(sqlUpdate,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());

        String sqlQueryGet = "SELECT id, name, description, release_date, duration, mpa " +
                "FROM public.films " +
                "WHERE id = ?";
        return jdbcTemplate.queryForObject(sqlQueryGet, this::mapRowToFilm, film.getId());
    }

    @Override
    public Collection<Film> list() {
        String sqlQueryGet = "SELECT id, name, description, release_date, duration, mpa " +
                "FROM public.films";
        return jdbcTemplate.query(sqlQueryGet, this::mapRowToFilm);
    }

    @Override
    public Optional<Film> findFilmById(Long id) {
        String sqlQueryGet = "SELECT id, name, description, release_date, duration, mpa " +
                "FROM public.films " +
                "WHERE id = ?";
        Optional<Film> result;
        try {
            result = Optional.ofNullable(jdbcTemplate.queryForObject(sqlQueryGet, this::mapRowToFilm, id));
        } catch (DataAccessException e) {
            result = Optional.empty();
        }
        return result;
    }

    @Override
    public boolean addLike(Long filmId, Long userId) {
        String sqlAdd = "INSERT INTO public.likes (film_id, user_id) " +
                "VALUES (?, ?)";
        return jdbcTemplate.update(sqlAdd, filmId, userId) > 0;
    }

    @Override
    public boolean deleteLike(Long filmId, Long userId) {
        String sqlDelete = "DELETE FROM public.likes " +
                "WHERE film_id = ? AND user_id = ?";
        return jdbcTemplate.update(sqlDelete, filmId, userId) > 0;
    }

    @Override
    public Collection<Film> getMostPopularFilms(int count) {
        String sqlGet = "SELECT id, name, description, release_date, duration, mpa " +
                "FROM public.films t1 " +
                "LEFT JOIN " +
                "(SELECT film_id, COUNT(user_id) count FROM public.likes GROUP BY film_id) t2 " +
                "ON t1.id = t2.film_id " +
                "ORDER BY count DESC " +
                "LIMIT ?";
        return jdbcTemplate.query(sqlGet, this::mapRowToFilm, count);
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
                .mpa(new Mpa(resultSet.getInt("mpa")))
                .build();
    }
}
