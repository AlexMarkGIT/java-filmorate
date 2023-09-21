package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;

    @Autowired
    public FilmService(InMemoryFilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public Collection<Film> findAll() {
        return filmStorage.getFilms().values();
    }

    public Film getFilmById(Integer id) {
        if (!filmStorage.containsById(id)) {
            throw new FilmNotFoundException(String.format("Фильм с id \"%d\" не найден", id));
        }
        return filmStorage.getFilmById(id);
    }

    public Film addFilm(Film film) {
        if (filmStorage.containsFilm(film)) {
            throw new FilmAlreadyExistException(String.format("Фильм \"%s\" уже добавлен", film.getName()));
        }
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        if (!filmStorage.containsById(film.getId())) {
            throw new FilmNotFoundException(String.format("Фильм с id \"%d\" не найден", film.getId()));
        }
        return filmStorage.updateFilm(film);
    }

    public void addLike(int filmId, int userId) {
        if (!filmStorage.containsById(filmId)) {
            throw new FilmNotFoundException(String.format("Фильма c id №%d не обнаружено", filmId));
        }
        if (!userService.containsById(userId)) {
            throw new UserNotFoundException(String.format("Пользователя c id №%d не обнаружено", userId));
        }
        filmStorage.addLike(filmId, userId);
    }

    public void deleteLike(int filmId, int userId) {
        if (!filmStorage.containsById(filmId)) {
            throw new FilmNotFoundException(String.format("Фильма c id №%d не обнаружено", filmId));
        }
        if (!userService.containsById(userId)) {
            throw new UserNotFoundException(String.format("Пользователя c id №%d не обнаружено", userId));
        }
        filmStorage.deleteLike(filmId, userId);
    }

    public Collection<Film> getPopularFilms(int count) {
        return filmStorage.getFilms().values().stream()
                .sorted(this::compare)
                .limit(count)
                .collect(Collectors.toList());
    }

    private int compare(Film f0, Film f1) {
        int result = f0.getLikes().size() - (f1.getLikes().size());
        return result * (-1);
    }
}
