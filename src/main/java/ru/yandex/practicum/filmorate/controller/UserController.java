package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Response;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.implementation.UserServiceStandard;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
public class UserController {
    /**
     * Обработка ззапросов для работы с фильмами.
     */
    private final UserServiceStandard userService;
    @PostMapping
    public ResponseEntity<Response> saveUser(@Valid @RequestBody User user) {
        User savedUser;
        String message;
        HttpStatus status;
        try {
            savedUser = userService.addUser(user);
            message = "User added";
            log.info("User added with id {}", user.getId());
            status = HttpStatus.OK;
        } catch (ValidationException e) {
            savedUser = null;
            message = e.getMessage();
            status = HttpStatus.BAD_REQUEST;
            log.info("User not added with exception: {}", e.getMessage());
        }
        return ResponseEntity.ok(
                Response.builder()
                        .time(LocalDateTime.now())
                        .message(message)
                        .status(status)
                        .statusCode(status.value())
                        .data(Map.of("user", savedUser == null ? "" : savedUser))
                        .build()
        );
    }

    @PutMapping
    public ResponseEntity<Response> updateUser(@Valid @RequestBody User user) {

        User uppdatedUser;
        String message;
        HttpStatus status;
        try {
            uppdatedUser = userService.updateUser(user);
            message = "User updated";
            log.info("User updated with id {}", user.getId());
            status = HttpStatus.OK;
        } catch (ValidationException e) {
            uppdatedUser = null;
            message = e.getMessage();
            status = HttpStatus.BAD_REQUEST;
            log.info("User not updated with exception {}", user.getId());
        }

        return ResponseEntity.ok(
                Response.builder()
                        .time(LocalDateTime.now())
                        .message(message)
                        .status(status)
                        .statusCode(status.value())
                        .data(Map.of("user", uppdatedUser == null ? "" : uppdatedUser))
                        .build()
        );
    }

    @GetMapping
    public ResponseEntity<Response> getAllUsers() {
        log.info("Getting all users list");
        return ResponseEntity.ok(
                Response.builder()
                        .time(LocalDateTime.now())
                        .message("All users")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .data(Map.of("users", userService.listUsers()))
                        .build()
        );
    }
}
