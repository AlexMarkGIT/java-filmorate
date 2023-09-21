package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.FilmServiceDefault;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@Slf4j
public class FilmController {

    private final FilmService filmService;

    public FilmController(FilmServiceDefault filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/films")
    public Collection<Film> findAll() {
        log.debug("Текущее количество фильмов: {}", filmService.findAll().size());
        return filmService.findAll();
    }

    @GetMapping(value = "/films/{id}")
    public Film findById(@PathVariable("id") Integer filmId) {
        return filmService.getFilmById(filmId);
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
}
