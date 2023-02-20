package ru.yandex.practicum.filmorate.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserStorage;
import ru.yandex.practicum.filmorate.repository.implementation.InMemoryUserRepository;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceStandard implements UserService {
    /**
     * Бизнес-логика работы с пользователями.
     */
    private final UserStorage userRepository;

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
    public User updateUser(User user) throws ValidationException, NotFoundException {
        if (user.getId() == null)
            throw new ValidationException("Не указан id пользователя!");
        if (userRepository.findUserById(user.getId()).isEmpty())
            throw new NotFoundException("Не существует пользователя с таким id");
        if (!isValidUser(user))
            throw new ValidationException("Неверно введены email, login или дата рождения!");
        if (user.getName() == null || user.getName().isBlank()) user.setName(user.getLogin());
        return userRepository.update(user);
    }

    @Override
    public User getUser(Long id) throws NotFoundException {
       if (userRepository.findUserById(id).isEmpty())
           throw new NotFoundException("Не существует пользователя с таким id");
       return userRepository.findUserById(id).get();
    }

    @Override
    public Collection<User> listUsers() {
        return userRepository.list();
    }

    @Override
    public boolean addFriend(Long id, Long friendId) throws NotFoundException, ValidationException {
        if (userRepository.findUserById(id).isEmpty() || userRepository.findUserById(friendId).isEmpty())
            throw new NotFoundException("Не существует пользователя с таким id");
        if (Objects.equals(id, friendId))
            throw new ValidationException("Пользователь не может быть другом сам себе");
        return userRepository.addFriend(id, friendId);
    }

    @Override
    public boolean deleteFriend(Long id, Long friendId) throws NotFoundException, ValidationException {
        if (userRepository.findUserById(id).isEmpty() || userRepository.findUserById(friendId).isEmpty())
            throw new NotFoundException("Не существует пользователя с таким id");
        if (Objects.equals(id, friendId))
            throw new ValidationException("Пользователь не может быть другом сам себе");
        return userRepository.deleteFriend(id, friendId);
    }

    @Override
    public Collection<User> getCommonFriends(Long id, Long userId) throws NotFoundException, ValidationException {
        if (userRepository.findUserById(id).isEmpty() || userRepository.findUserById(userId).isEmpty())
            throw new NotFoundException("Не существует пользователя с таким id");
        if (Objects.equals(id, userId))
            throw new ValidationException("Нельзя посмотреть общих друзей");
        return userRepository.getCommonFriends(id, userId);
    }

    @Override
    public Collection<User> getFriends(Long id) throws NotFoundException {
        if (userRepository.findUserById(id).isEmpty())
            throw new NotFoundException("Не существует пользователя с таким id");
        return userRepository.getFriends(id);
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
