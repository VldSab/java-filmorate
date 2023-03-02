package ru.yandex.practicum.filmorate.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.constants.GenreNames;
import ru.yandex.practicum.filmorate.model.constants.MpaNames;
import ru.yandex.practicum.filmorate.repository.InformationRepository;
import ru.yandex.practicum.filmorate.service.InformationService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InformationServiceStandard implements InformationService {

    private final InformationRepository informationRepository;

    @Override
    public Mpa getMpa(int id) throws NotFoundException {
        Mpa mpa = informationRepository.getMpaById(id);
        if (MpaNames.getNameById(id) == null)
            throw new NotFoundException(String.format("MPA с ID %s не найден", id));
        return mpa;
    }

    @Override
    public Genre getGenre(int id) throws NotFoundException {
        Genre genre = informationRepository.getGenreById(id);
        if (GenreNames.getNameById(id) == null)
            throw new NotFoundException(String.format("Жанр с ID %s не найден", id));
        return genre;
    }

    @Override
    public List<Mpa> listMpa() {
        return informationRepository.getAllMpas();
    }

    @Override
    public List<Genre> listGenre() {
        return informationRepository.getAllGenres();
    }
}
