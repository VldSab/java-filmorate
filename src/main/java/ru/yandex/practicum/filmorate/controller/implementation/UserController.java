package ru.yandex.practicum.filmorate.controller.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.FilmrateController;
import ru.yandex.practicum.filmorate.exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Response;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.implementation.UserServiceStandard;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
public class UserController extends FilmrateController {
    /**
     * Обработка ззапросов для работы с фильмами.
     */
    private final UserServiceStandard userService;
    @PostMapping
    public ResponseEntity<?> saveUser(@Valid @RequestBody User user) {
        User savedUser;
        HttpStatus status;
        try {
            savedUser = userService.addUser(user);
            log.info("User added with id {}", user.getId());
            status = HttpStatus.OK;
        } catch (ValidationException e) {
            savedUser = null;
            status = HttpStatus.BAD_REQUEST;
            log.info("User not added with exception: {}", e.getMessage());
        }
        return createRawResponse(status, savedUser);
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@Valid @RequestBody User user) {

        User uppdatedUser;
        HttpStatus status;
        try {
            uppdatedUser = userService.updateUser(user);
            log.info("User updated with id {}", user.getId());
            status = HttpStatus.OK;
        } catch (ValidationException e) {
            uppdatedUser = null;
            status = HttpStatus.BAD_REQUEST;
            log.info("User not updated with exception {}", user.getId());
        } catch (NotFoundException e) {
            uppdatedUser = null;
            status = HttpStatus.NOT_FOUND;
        }

        return createRawResponse(status, uppdatedUser);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public ResponseEntity<?> addFriend(@PathVariable Long id, @PathVariable Long friendId) throws ValidationException, NotFoundException {
        if (id == null || friendId == null)
            throw new NullPointerException("Не передан id пользователя или id друга");
        boolean isSuccess = userService.addFriend(id, friendId);
        return createRawResponse(HttpStatus.OK, isSuccess);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity<?> deleteFriend(@PathVariable Long id, @PathVariable Long friendId) throws ValidationException, NotFoundException {
        if (id == null || friendId == null)
            throw new NullPointerException("Не передан id пользователя или id друга");
        boolean isSuccess = userService.deleteFriend(id, friendId);
        return createRawResponse(HttpStatus.OK, isSuccess);
    }

    @GetMapping("/{id}/friends")
    public ResponseEntity<?> getUserFriends(@PathVariable Long id) throws NotFoundException {
        if (id == null)
            throw new NullPointerException("Не передам id пользователя");
        return createRawResponse(HttpStatus.OK, userService.getFriends(id));
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public ResponseEntity<?> getCommonFriends(@PathVariable Long id, @PathVariable("otherId") Long userId) throws ValidationException, NotFoundException {
        if (id == null || userId == null)
            throw new NullPointerException("Не передан id первого или второго пользователя");
        return createRawResponse(HttpStatus.OK, userService.getCommonFriends(id, userId));
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        log.info("Getting all users list");
        return createRawResponse(HttpStatus.OK, userService.listUsers());
    }

}
