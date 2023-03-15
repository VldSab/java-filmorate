package ru.yandex.practicum.filmorate.controller.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.controller.FilmrateController;
import ru.yandex.practicum.filmorate.exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.service.InformationService;

@RestController
@RequiredArgsConstructor
public class InformationController extends FilmrateController {

    private final InformationService informationService;

    @GetMapping("/mpa/{id}")
    ResponseEntity<?> mpa(@PathVariable int id) throws NotFoundException {
        return createRawResponse(HttpStatus.OK, informationService.getMpa(id));
    }

    @GetMapping("/genres/{id}")
    ResponseEntity<?> genre(@PathVariable int id) throws NotFoundException {
        return createRawResponse(HttpStatus.OK, informationService.getGenre(id));
    }

    @GetMapping("/mpa")
    ResponseEntity<?> listMpa() {
        return createRawResponse(HttpStatus.OK, informationService.listMpa());
    }

    @GetMapping("/genres")
    ResponseEntity<?> listGenres() {
        return createRawResponse(HttpStatus.OK, informationService.listGenre());
    }

}

