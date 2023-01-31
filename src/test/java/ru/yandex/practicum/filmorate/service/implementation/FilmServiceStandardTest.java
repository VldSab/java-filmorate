package ru.yandex.practicum.filmorate.service.implementation;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.implementation.InMemoryFilmRepository;
import ru.yandex.practicum.filmorate.repository.implementation.InMemoryUserRepository;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmServiceStandardTest {
    private static FilmServiceStandard serviceStandard;

    @BeforeAll
    public static void setServiceStandard() {
        InMemoryFilmRepository filmRepository = new InMemoryFilmRepository();
        InMemoryUserRepository userRepository = new InMemoryUserRepository();
        serviceStandard = new FilmServiceStandard(filmRepository, userRepository);
    }

    private final Film validFilm = Film.builder()
            .id(1L)
            .name("Name")
            .duration(90L)
            .releaseDate(LocalDate.of(2020, 11, 12))
            .build();

    private final Film invalidFilmOfDuration = Film.builder()
            .id(2L)
            .name("Name2")
            .duration(-90L)
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