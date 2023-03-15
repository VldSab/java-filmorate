package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.*;


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
    Mpa mpa;
    List<Genre> genres = new ArrayList<>();
    Set<Long> likes = new HashSet<>();

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("name", name);
        values.put("description", description);
        values.put("release_date", releaseDate);
        values.put("duration", duration);
        values.put("mpa_id", mpa.getId());
        return values;
    }

}
