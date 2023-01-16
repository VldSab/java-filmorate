package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserService {
    User addUser(User user) throws ValidationException;

    User updateUser(User user) throws ValidationException;

    Collection<User> listUsers();
}
