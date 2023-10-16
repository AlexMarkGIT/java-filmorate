package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.FilmServiceImpl;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@Slf4j
public class FilmController {

    private final FilmService filmService;

    public FilmController(FilmServiceImpl filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/films")
    public Collection<Film> findAll() {
        log.debug("Текущее количество фильмов: {}", filmService.findAll().size());
        return filmService.findAll();
    }

    @GetMapping(value = "/films/{id}")
    public Film findById(@PathVariable("id") Integer filmId) {
        return filmService.getById(filmId);
    }

    @PostMapping(value = "/films")
    public Film create(@RequestBody @Valid Film film) {
        log.debug("Фильм " + film.getName() + " добавлен");
        return filmService.addFilm(film);
    }

    @PutMapping(value = "/films")
    public Film update(@RequestBody @Valid Film film) {
        log.debug("Фильм с номером id=" + film.getId() + " обновлен");
        return filmService.updateFilm(film);
    }

    @GetMapping("/films/popular")
    public Collection<Film> getPopular(@RequestParam(defaultValue = "10", required = false) Integer count) {
        return filmService.getPopularFilms(count);
    }

    @PutMapping(value = "/films/{filmId}/like/{userId}")
    public void addLike(@PathVariable("filmId") Integer filmId, @PathVariable("userId") Integer userId) {
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping(value = "/films/{filmId}/like/{userId}")
    public void deleteLike(@PathVariable("filmId") Integer filmId, @PathVariable("userId") Integer userId) {
        filmService.deleteLike(filmId, userId);
    }

    @GetMapping(value = "/mpa")
    public List<MPARating> findAllMPA() {
        return filmService.getAllMPA();
    }

    @GetMapping(value = "/mpa/{id}")
    public MPARating findMPAById(@PathVariable("id") Integer id) {
        return filmService.getMPAById(id);
    }

    @GetMapping(value = "/genres")
    public List<Genre> findAllGenres() {
        return filmService.getAllGenres();
    }

    @GetMapping(value = "/genres/{id}")
    public Genre findGenreById(@PathVariable("id") Integer id) {
        return filmService.getGenreById(id);
    }
}
