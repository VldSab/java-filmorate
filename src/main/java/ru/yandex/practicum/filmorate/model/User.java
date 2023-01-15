package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
@Data
@NoArgsConstructor
public class User {
    /**
     * Модель польователя.
     */

    @NonNull
    Long id;
    @Email
    String email;
    @NotBlank
    String login;
    String name;
    LocalDate birthday;
}
