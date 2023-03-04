package ru.yandex.practicum.filmorate.repository.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.InformationRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class InformationRepositoryStandard implements InformationRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Mpa getMpaById(int id) {
        String sqlGet = "SELECT id, name FROM public.mpa " +
                "WHERE id = ?";
        Mpa result;
        try {
            result = jdbcTemplate.queryForObject(sqlGet, this::mapRowToMpa, id);
        } catch (DataAccessException e) {
            result = null;
        }
        return result;
    }

    @Override
    public Genre getGenreById(int id) {
        String sqlGet = "SELECT id, name FROM public.genres " +
                "WHERE id = ?";
        Genre result;
        try {
            result = jdbcTemplate.queryForObject(sqlGet, this::mapRowToGenre, id);
        } catch (DataAccessException e) {
            result = null;
        }
        return result;
    }

    @Override
    public List<Mpa> getAllMpas() {
        String sqlGetAll = "SELECT * FROM public.mpa";
        return jdbcTemplate.query(sqlGetAll, this::mapRowToMpa);
    }

    @Override
    public List<Genre> getAllGenres() {
        String sqlGetAll = "SELECT * FROM public.genres";
        return jdbcTemplate.query(sqlGetAll, this::mapRowToGenre);
    }

    private Mpa mapRowToMpa(ResultSet resultSet, int rowNumber) throws SQLException {
        return Mpa.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .build();
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNumber) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .build();
    }
}
