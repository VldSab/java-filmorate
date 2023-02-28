package ru.yandex.practicum.filmorate.repository.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

@Repository("dbUserRepository")
@RequiredArgsConstructor
public class DBUserRepository implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public User save(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        Long id = simpleJdbcInsert.executeAndReturnKey(user.toMap()).longValue();

        String sqlQueryGet = "SELECT id, name, email, login, birthday " +
                "FROM public.users " +
                "WHERE id = ?";

        return jdbcTemplate.queryForObject(sqlQueryGet, this::mapRowToUser, id);
    }

    private User mapRowToUser(ResultSet resultSet, int rowNumber) throws SQLException {
        LocalDate birthday;
        try {
            birthday = resultSet.getDate("birthday").toLocalDate();
        } catch (NullPointerException e) {
            birthday = null;
        }
        return User.builder()
                .id(resultSet.getLong("id"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("name"))
                .birthday(birthday)
                .build();
    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public Collection<User> list() {
        return null;
    }

    @Override
    public Optional<User> findUserById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<User> findUserByLogin(String login) {
        return Optional.empty();
    }

    @Override
    public boolean addFriend(Long id, Long friendId) {
        return false;
    }

    @Override
    public boolean deleteFriend(Long id, Long friendId) {
        return false;
    }

    @Override
    public Collection<User> getCommonFriends(Long id, Long userId) {
        return null;
    }

    @Override
    public Collection<User> getFriends(Long id) {
        return null;
    }
}
