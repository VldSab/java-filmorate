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
        if (user.getName() == null) user.setName(user.getLogin());
        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user, Long id) throws ValidationException {
        if (!isValidUser(user))
            throw new ValidationException("Неверно введены email, login или дата рождения!");
        if (user.getName().isBlank()) user.setName(user.getLogin());
        return userRepository.update(user, id);
    }

    @Override
    public Collection<User> listUsers() {
        return userRepository.list();
    }

    private boolean isValidUser(User user) {
        boolean isValidEmail = user.getEmail().contains("@");
        boolean isValidLogin = !user.getLogin().contains(" ");
        boolean isValidBirthday = true;
        if (user.getBirthday() != null)
            isValidBirthday = user.getBirthday().isBefore(LocalDate.now()) && !user.getBirthday().isEqual(LocalDate.now());
        return isValidEmail && isValidLogin && isValidBirthday;
    }
}
