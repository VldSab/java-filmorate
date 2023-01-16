package ru.yandex.practicum.filmorate.repository;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Repository
public class InMemoryUserRepository {
    /**
     * Хранилище пользователей в оперативной памяти.
     */
    private final Map<Long, User> usersStorage = new HashMap();
    private final HashMap<String, Long> loginsStorage = new HashMap<>();


    public User save(User user) {
        Long id = usersStorage.size() + 1L;
        user.setId(id);
        usersStorage.put(id, user);
        loginsStorage.put(user.getLogin(), id);
        return user;
    }

    public User update(User user) {
        User userToUpdate = usersStorage.get(user.getId());
        if (userToUpdate == null) {
            save(user);
            return user;
        }
        userToUpdate.setName(user.getName());
        userToUpdate.setEmail(user.getEmail());
        userToUpdate.setBirthday(user.getBirthday());
        userToUpdate.setLogin(user.getLogin());
        return userToUpdate;
    }

    public Collection<User> list() {
        return usersStorage.values();
    }

    public Optional<User> findUserByLogin(String login) {
        return loginsStorage.containsKey(login)
                ? Optional.of(usersStorage.get(loginsStorage.get(login)))
                : Optional.empty();
    }

    public Optional<User> findUserById(Long id) {
        return usersStorage.containsKey(id)
                ? Optional.of(usersStorage.get(id))
                : Optional.empty();
    }
}
