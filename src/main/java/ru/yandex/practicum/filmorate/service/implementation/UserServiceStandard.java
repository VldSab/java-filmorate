package ru.yandex.practicum.filmorate.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.InMemoryUserRepository;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserServiceStandard implements UserService {
    /**
     * Бизнес-логика работы с пользователями.
     */
    private final InMemoryUserRepository userRepository;

    @Override
    public User addUser(User user) throws ValidationException {
        if (!isValidUser(user))
            throw new ValidationException("Неверно введены email, login или дата рождения!");
        if (userRepository.findUserByLogin(user.getLogin()).isPresent())
            throw new ValidationException("Такой login уже существует!");
        if (user.getName() == null || user.getName().isBlank()) user.setName(user.getLogin());
        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user) throws ValidationException {
        if (user.getId() == null)
            throw new ValidationException("Не указан id пользователя!");
        if (userRepository.findUserById(user.getId()).isEmpty())
            throw new ValidationException("Не существует пользователя с таким id");
        if (!isValidUser(user))
            throw new ValidationException("Неверно введены email, login или дата рождения!");
        if (user.getName() == null || user.getName().isBlank()) user.setName(user.getLogin());
        return userRepository.update(user);
    }

    @Override
    public Collection<User> listUsers() {
        return userRepository.list();
    }

    private boolean isValidUser(User user) {
        boolean isValidEmail = user.getEmail().contains("@");
        boolean isValidLogin = !user.getLogin().isBlank() && !user.getLogin().contains(" ");
        boolean isValidBirthday = true;
        if (user.getBirthday() != null)
            isValidBirthday = user.getBirthday().isBefore(LocalDate.now()) && !user.getBirthday().isEqual(LocalDate.now());
        return isValidEmail && isValidLogin && isValidBirthday;
    }
}
