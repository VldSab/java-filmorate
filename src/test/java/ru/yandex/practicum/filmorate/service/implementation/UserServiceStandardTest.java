package ru.yandex.practicum.filmorate.service.implementation;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.InMemoryUserRepository;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceStandardTest {

    private static InMemoryUserRepository userRepository;
    private static UserServiceStandard serviceStandard;

    @BeforeAll
    public static void setServiceStandard() {
        userRepository = new InMemoryUserRepository();
        serviceStandard = new UserServiceStandard(userRepository);
    }

    private final User validUser = User.builder()
            .id(1L)
            .login("login")
            .email("login@mail.ru")
            .birthday(LocalDate.of(2000, 3, 28))
            .build();

    private final User invalidUserEmail = User.builder()
            .id(1L)
            .login("login")
            .email("login")
            .birthday(LocalDate.of(2000, 3, 28))
            .build();

    private final User invalidUserBirthday = User.builder()
            .id(1L)
            .login("login")
            .email("login@mail.ru")
            .birthday(LocalDate.now())
            .build();

    @Test
    void addFilmTest() {
        assertDoesNotThrow(() -> serviceStandard.addUser(validUser));
        final ValidationException exceptionDuration = assertThrows(
                ValidationException.class,
                () -> serviceStandard.addUser(invalidUserEmail)
        );
        assertEquals("Неверно введены email, login или дата рождения!", exceptionDuration.getMessage());
        final ValidationException exceptionDate = assertThrows(
                ValidationException.class,
                () -> serviceStandard.addUser(invalidUserBirthday)
        );
        assertEquals("Неверно введены email, login или дата рождения!", exceptionDate.getMessage());
    }

}