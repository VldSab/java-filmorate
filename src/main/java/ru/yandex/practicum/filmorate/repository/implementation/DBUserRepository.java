package ru.yandex.practicum.filmorate.repository.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.constants.FriendsipStatus;
import ru.yandex.practicum.filmorate.repository.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

@Repository("dbUserRepository")
@RequiredArgsConstructor
@Slf4j
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

    @Override
    public User update(User user) {
        String sqlUpdate = "UPDATE public.users " +
                "SET login = ?, email = ?, name = ?, birthday = ? " +
                "WHERE id = ?";

        jdbcTemplate.update(sqlUpdate,
                user.getLogin(),
                user.getEmail(),
                user.getName(),
                user.getBirthday(),
                user.getId());

        String sqlQueryGet = "SELECT id, login, email, name, birthday " +
                "FROM public.users " +
                "WHERE id = ?";

        return jdbcTemplate.queryForObject(sqlQueryGet, this::mapRowToUser, user.getId());
    }

    @Override
    public Collection<User> list() {
        String sqlGet = "SELECT id, login, email, name, birthday " +
                "FROM public.users";
        return jdbcTemplate.query(sqlGet, this::mapRowToUser);
    }

    @Override
    public Optional<User> findUserById(Long id) {
        String sqlGet = "SELECT id, login, email, name, birthday " +
                "FROM public.users " +
                "WHERE id = ?";
        Optional<User> result;
        try {
            result = Optional.ofNullable(jdbcTemplate.queryForObject(sqlGet, this::mapRowToUser, id));
        } catch (DataAccessException e) {
            result = Optional.empty();
            log.info("Запрошен несуществующий id");
        }
        return result;
    }

    @Override
    public Optional<User> findUserByLogin(String login) {
        String sqlGet = "SELECT id, login, email, name, birthday " +
                "FROM public.users " +
                "WHERE login = ?";
        Optional<User> result;
        try {
            result = Optional.ofNullable(jdbcTemplate.queryForObject(sqlGet, this::mapRowToUser, login));
        } catch (DataAccessException e) {
            result = Optional.empty();
            log.info("Запрошен несуществующий логин");
        }
        return result;
    }

    @Override
    public boolean addFriend(Long id, Long friendId) {
        String sqlAddFriend = "INSERT INTO public.friendship (user_id, friend_id, is_confirmed) " +
                "VALUES (?, ?, ?)";
        try {
            jdbcTemplate.update(sqlAddFriend,
                    id,
                    friendId,
                    FriendsipStatus.NOT_CONFIRMED.name());
        } catch (DataAccessException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteFriend(Long id, Long friendId) {
        String sqlDelete = "DELETE FROM public.friendship " +
                "WHERE user_id = ? AND friend_id = ?";
        return jdbcTemplate.update(sqlDelete, id, friendId) > 0;
    }

    @Override
    public Collection<User> getCommonFriends(Long id, Long userId) {
        String sqlGetFriends = "SELECT id, email, login, name, birthday " +
                "FROM public.users " +
                "WHERE id IN " +
                "(SELECT friend_id FROM friendship WHERE user_id = ?) " +
                "AND id IN " +
                "(SELECT friend_id FROM friendship WHERE user_id = ?)";
        return jdbcTemplate.query(sqlGetFriends, this::mapRowToUser, id, userId);
    }

    @Override
    public Collection<User> getFriends(Long id) {
        String sqlGetFriends = "SELECT id, email, login, name, birthday " +
                "FROM public.users " +
                "WHERE id IN " +
                "(SELECT friend_id FROM friendship WHERE user_id = ?)";

        return jdbcTemplate.query(sqlGetFriends, this::mapRowToUser, id);
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
}
