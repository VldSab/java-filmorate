package ru.yandex.practicum.filmorate.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmStorage;
import ru.yandex.practicum.filmorate.repository.UserStorage;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class FilmServiceStandard implements FilmService {
    /**
     * Бизнес-логика работы с фильмами.
     */
    private final static Long MAX_DESCRIPTION_LENGTH = 200L;
    private final static LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private final FilmStorage filmRepository;
    private final UserStorage userRepository;

    @Override
    public Film addFilm(Film film) throws ValidationException {
        if (!isValidFilm(film)) throw new ValidationException("Неверно указаны данные о фильме!");
        return filmRepository.save(film);
    }

    @Override
    public Film updateFilm(Film film) throws ValidationException, NotFoundException {
        if (filmRepository.findFilmById(film.getId()).isEmpty())
            throw new NotFoundException("Нет фильма с таким id");
        if (!isValidFilm(film))
            throw new ValidationException("Неверно указаны данные о фильме!");
        return filmRepository.update(film);
    }

    @Override
    public Film getFilm(Long id) throws NotFoundException {
        if (filmRepository.findFilmById(id).isEmpty())
            throw new NotFoundException("Не существует фильма с таким id");
        return filmRepository.findFilmById(id).get();
    }

    @Override
    public Collection<Film> listFilms() {
        return filmRepository.list();
    }

    @Override
    public boolean addLike(Long filmId, Long userId) throws NotFoundException {
        if (filmRepository.findFilmById(filmId).isEmpty())
            throw new NotFoundException("Нет фильма с таким id");
        if (userRepository.findUserById(userId).isEmpty())
            throw new NotFoundException("Нет пользователя с таким id");
        return filmRepository.addLike(filmId, userId);
    }

    @Override
    public boolean deleteLike(Long filmId, Long userId) throws NotFoundException {
        if (filmRepository.findFilmById(filmId).isEmpty())
            throw new NotFoundException("Нет фильма с таким id");
        if (userRepository.findUserById(userId).isEmpty())
            throw new NotFoundException("Нет пользователя с таким id");
        return filmRepository.deleteLike(filmId, userId);
    }

    @Override
    public Collection<Film> getMostPopularFilms(int count) {
        return filmRepository.getMostPopularFilms(count);
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
            isValidDuration = film.getDuration() > 0;
        return isValidLength && isValidDate && isValidDuration;
    }
}
