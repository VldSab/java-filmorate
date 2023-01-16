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

    public User save(User user) {
        usersStorage.put(user.getId(), user);
        return user;
    }

    public User update(User user, Long id) {
        User userToUpdate = usersStorage.get(id);
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
}
