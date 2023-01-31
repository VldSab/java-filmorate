package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Data
@NoArgsConstructor
@SuperBuilder
public class Film {
    /**
     * Модель фильма.
     */
    Long id;
    @NotBlank
    String name;
    String description;
    LocalDate releaseDate;
    Long duration;
    Set<Long> likes = new HashSet<>();

}
