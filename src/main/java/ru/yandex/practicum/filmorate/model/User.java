package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.yandex.practicum.filmorate.annotations.ValidEmail;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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
    Set<Long> friends = new HashSet<>();

}
