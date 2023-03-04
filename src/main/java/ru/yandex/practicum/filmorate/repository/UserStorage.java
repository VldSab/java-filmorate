package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {
    User save(User user);
    User update(User user);
    Collection<User> list();
    Optional<User> findUserById(Long id);
    Optional<User> findUserByLogin(String login);
    boolean addFriend(Long id, Long friendId);
    boolean deleteFriend(Long id, Long friendId);
    Collection<User> getCommonFriends(Long id, Long userId);
    Collection<User> getFriends(Long id);
}
