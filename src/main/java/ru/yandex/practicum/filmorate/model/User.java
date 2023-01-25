package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import ru.yandex.practicum.filmorate.annotations.ValidEmail;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
@Data
@NoArgsConstructor
@SuperBuilder
public class User {
    /**
     * Модель польователя.
     */
    Long id;
    @ValidEmail
    String email;
    @NotBlank
    String login;
    String name;
    LocalDate birthday;
}
