package ru.yandex.practicum.filmorate.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.InMemoryFilmRepository;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class FilmServiceStandard implements FilmService {

    private final static Long MAX_DESCRIPTION_LENGTH = 200L;
    private final static LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private final InMemoryFilmRepository filmRepository;

    @Override
    public Film addFilm(Film film) throws ValidationException {
        if (!isValidFilm(film)) throw new ValidationException("Неверно указаны данные о фильме!");
        return filmRepository.save(film);
    }

    @Override
    public Film updateFilm(Film film, Long id) throws ValidationException {
        if (!isValidFilm(film)) throw new ValidationException("Неверно указаны данные о фильме!");
        return filmRepository.update(film, id);
    }

    @Override
    public Collection<Film> listFilms() {
        return filmRepository.list();
    }

    private boolean isValidFilm(Film film) {
        boolean isValidLength = true;
        if (film.getDescription() != null)
            isValidLength = film.getDescription().length() <= MAX_DESCRIPTION_LENGTH;
        boolean isValidDate = true;
        if (film.getReleaseDate() != null)
            isValidDate = film.getReleaseDate().isAfter(MIN_RELEASE_DATE);
        boolean isValidDuration = true;
        if (film.getDuration() != null)
            isValidDuration = !film.getDuration().isNegative();
        return isValidLength && isValidDate && isValidDuration;
    }
}
