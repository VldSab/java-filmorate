package ru.yandex.practicum.filmorate.service.implementation;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.InMemoryFilmRepository;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmServiceStandardTest {
    private static InMemoryFilmRepository filmRepository;
    private static FilmServiceStandard serviceStandard;

    @BeforeAll
    public static void setServiceStandard() {
        filmRepository = new InMemoryFilmRepository();
        serviceStandard = new FilmServiceStandard(filmRepository);
    }

    private final Film validFilm = Film.builder()
            .id(1L)
            .name("Name")
            .duration(Duration.ofMinutes(90))
            .releaseDate(LocalDate.of(2020, 11, 12))
            .build();

    private final Film invalidFilmOfDuration = Film.builder()
            .id(2L)
            .name("Name2")
            .duration(Duration.ofMinutes(-90))
            .build();

    private final Film invalidFilmOfDate= Film.builder()
            .id(3L)
            .name("Name2")
            .releaseDate(LocalDate.of(1895, 12, 27))
            .build();

    @Test
    void addFilmTest() {
        assertDoesNotThrow(() -> serviceStandard.addFilm(validFilm));
        final ValidationException exceptionDuration = assertThrows(
                ValidationException.class,
                () -> serviceStandard.addFilm(invalidFilmOfDuration)
        );
        assertEquals("Неверно указаны данные о фильме!", exceptionDuration.getMessage());
        final ValidationException exceptionDate = assertThrows(
                ValidationException.class,
                () -> serviceStandard.addFilm(invalidFilmOfDate)
        );
        assertEquals("Неверно указаны данные о фильме!", exceptionDate.getMessage());
    }

}