package ru.yandex.practicum.filmorate.repository.implementation;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserStorage;

import java.util.*;

@Repository
public class InMemoryUserRepository implements UserStorage {
    /**
     * Хранилище пользователей в оперативной памяти.
     */
    private final Map<Long, User> usersStorage = new HashMap<>();
    private final HashMap<String, Long> loginsStorage = new HashMap<>();
    private static Long id = 1L;

    public User save(User user) {
        user.setId(id);
        usersStorage.put(id, user);
        loginsStorage.put(user.getLogin(), id);
        id += 1;
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

    @Override
    public boolean addFriend(Long id, Long friendId) {
        User user = usersStorage.get(id);
        User friend = usersStorage.get(friendId);
        return user.getFriends().add(friend) && friend.getFriends().add(user);
    }

    @Override
    public boolean deleteFriend(Long id, Long friendId) {
        User user = usersStorage.get(id);
        User friend = usersStorage.get(friendId);
        return user.getFriends().remove(friend) && friend.getFriends().remove(user);
    }

    @Override
    public Collection<User> getCommonFriends(Long id, Long userId) {
        User user = usersStorage.get(id);
        User secondUser = usersStorage.get(userId);
        Set<User> intersection = new HashSet<>(user.getFriends());
        intersection.retainAll(secondUser.getFriends());
        return intersection;
    }

    @Override
    public Collection<User> getFriends(Long id) {
        return usersStorage.get(id).getFriends();
    }

    public Optional<User> findUserById(Long id) {
        return usersStorage.containsKey(id)
                ? Optional.of(usersStorage.get(id))
                : Optional.empty();
    }
}
