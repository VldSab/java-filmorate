package ru.yandex.practicum.filmorate.repository.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.constants.MpaNames;
import ru.yandex.practicum.filmorate.repository.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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

        String values = film.getGenres().stream().map(
                it -> String.format("(%s, %s)", id, it.getId())
                //it -> jdbcTemplate.update(sqlQuerySetGenre, id, it.getId())
        ).collect(Collectors.joining(", "));

        String sqlQuerySetGenre = "INSERT INTO public.film_genres (film_id, genre_id) " +
                "VALUES " + values;
        if (!values.isBlank())
            jdbcTemplate.update(sqlQuerySetGenre);
        film.setId(id);
        return film;
    }

    @Override
    public Film update(Film film) {
        // обновление таблицы фильмов
        String sqlUpdate = "UPDATE public.films " +
                "SET name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? " +
                "WHERE id = ?";
        jdbcTemplate.update(sqlUpdate,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());

        // обновление таблицы жанров фильмов
        String sqlQuerySetGenre = "INSERT INTO public.film_genres (film_id, genre_id) " +
                "VALUES (?, ?)";
        String sqlQueryDeleteGenre = "DELETE FROM public.film_genres WHERE film_id = ?";

        Set<Genre> genreSet = new HashSet<>(film.getGenres());

        jdbcTemplate.update(sqlQueryDeleteGenre, film.getId());
        genreSet.forEach(
                it -> jdbcTemplate.update(sqlQuerySetGenre, film.getId(), it.getId())
        );


        String sqlQueryGet = "SELECT id, name, description, release_date, duration, mpa_id " +
                "FROM public.films " +
                "WHERE id = ?";
        Film result = jdbcTemplate.queryForObject(sqlQueryGet, this::mapRowToFilm, film.getId());
        result.setGenres(genreSet.stream().sorted(Comparator.comparingInt(it -> it.getId())).collect(Collectors.toList()));

        return result;
    }

    @Override
    public Collection<Film> list() {
        String sqlQueryGet = "SELECT id, name, description, release_date, duration, mpa_id " +
                "FROM public.films";
        String sqlGetGenres = "SELECT g.id, g.name " +
                "FROM public.film_genres fg " +
                "LEFT JOIN genres g ON fg.genre_id = g.id " +
                "WHERE fg.film_id = ?";

        List<Film> films = jdbcTemplate.query(sqlQueryGet, this::mapRowToFilm);
        films.forEach(
                it -> {
                    Set<Genre> genres = new HashSet<>(jdbcTemplate.query(sqlGetGenres, this::mapRowToGenre, it.getId()));
                    it.setGenres(genres.stream().sorted(Comparator.comparingInt(g -> g.getId())).collect(Collectors.toList()));
                }
        );
        return films;
    }

    @Override
    public Optional<Film> findFilmById(Long id) {
        String sqlQueryGet = "SELECT id, name, description, release_date, duration, mpa_id " +
                "FROM public.films " +
                "WHERE id = ?";
        String sqlGetGenres = "SELECT g.id, g.name " +
                "FROM public.film_genres fg " +
                "LEFT JOIN genres g ON fg.genre_id = g.id " +
                "WHERE fg.film_id = ?";

        Optional<Film> result;
        try {
            result = Optional.ofNullable(jdbcTemplate.queryForObject(sqlQueryGet, this::mapRowToFilm, id));
            result.ifPresent(film -> {
                Set<Genre> genres = new HashSet<>(jdbcTemplate.query(sqlGetGenres, this::mapRowToGenre, id));
                film.setGenres(genres.stream().sorted(Comparator.comparingInt(it -> it.getId())).collect(Collectors.toList()));
            });
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
        String sqlGet = "SELECT id, name, description, release_date, duration, mpa_id " +
                "FROM public.films t1 " +
                "LEFT JOIN " +
                "(SELECT film_id, COUNT(user_id) count FROM public.likes GROUP BY film_id) t2 " +
                "ON t1.id = t2.film_id " +
                "ORDER BY count DESC " +
                "LIMIT ?";

        List<Film> films = jdbcTemplate.query(sqlGet, this::mapRowToFilm, count);

        String sqlGetGenres = "SELECT g.id, g.name " +
                "FROM public.film_genres fg " +
                "LEFT JOIN genres g ON fg.genre_id = g.id " +
                "WHERE fg.film_id = ?";

        films.forEach(
                it -> it.setGenres(jdbcTemplate.query(sqlGetGenres, this::mapRowToGenre, it.getId()))
        );
        return films;
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNumber) throws SQLException {
        LocalDate releaseDate;
        try {
            releaseDate = resultSet.getDate("release_date").toLocalDate();
        } catch (NullPointerException e) {
            releaseDate = null;
        }
        int mpaId = resultSet.getInt("mpa_id");
        return Film.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(releaseDate)
                .duration(resultSet.getLong("duration"))
                .mpa(new Mpa(mpaId, MpaNames.getNameById(mpaId)))
                .build();
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNumber) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .build();
    }
}
