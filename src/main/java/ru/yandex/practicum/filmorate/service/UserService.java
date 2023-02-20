package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserService {
    User addUser(User user) throws ValidationException;
    User updateUser(User user) throws ValidationException, NotFoundException;

    User getUser(Long id) throws NotFoundException;
    Collection<User> listUsers();
    boolean addFriend(Long id, Long friendId) throws NotFoundException, ValidationException;
    boolean deleteFriend(Long id, Long friendId) throws NotFoundException, ValidationException;
    Collection<User> getCommonFriends(Long id, Long userId) throws NotFoundException, ValidationException;
    Collection<User> getFriends(Long id) throws NotFoundException;

}
