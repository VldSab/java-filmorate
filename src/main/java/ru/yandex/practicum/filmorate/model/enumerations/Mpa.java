package ru.yandex.practicum.filmorate.model.enumerations;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Mpa {
    @JsonProperty("id")
    int id;
}
