package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class FilmController {

    private final Map<Integer, Film> films = new HashMap<>();

    private int filmIdentifier = 0;

    @GetMapping("/films")
    public Collection<Film> findAll() {
        log.debug("Текущее количество пользователей: {}", films.size());
        return films.values();
    }

    @PostMapping(value = "/films")
    public Film create(@RequestBody @Valid Film film) {

        if (films.containsValue(film)) {
            throw new ValidationException("Такой фильм уже существует");
        }

        film.setId(++filmIdentifier);
        films.put(film.getId(), film);
        log.debug("Фильм " + film.getName() + " создан, id=" + film.getId());
        return film;
    }

    @PutMapping(value = "/films")
    public Film update(@Valid @RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Фильма с номером " + film.getId() + " не существует");
        }

        films.put(film.getId(), film);
        log.debug("Фильм с номером id=" + film.getId() + " обновлен");
        return film;
    }

}
