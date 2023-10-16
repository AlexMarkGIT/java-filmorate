package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.storage.*;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmServiceDefault implements FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;
    private final MPAStorage mpaStorage;
    private final GenreStorage genreStorage;

    public FilmServiceDefault(FilmStorageDB filmStorage,
                              UserServiceDefault userService,
                              MPAStorageDB mpaStorage,
                              GenreStorageDB genreStorage) {
        this.filmStorage = filmStorage;
        this.userService = userService;
        this.mpaStorage = mpaStorage;
        this.genreStorage = genreStorage;
    }

    public Collection<Film> findAll() {
        return filmStorage.getFilms();
    }

    public Film getFilmById(Integer id) {
        if (!filmStorage.containsById(id)) {
            throw new FilmNotFoundException(String.format("Фильм с id \"%d\" не найден", id));
        }
        return filmStorage.getFilmById(id);
    }

    public Film addFilm(Film film) {
        /*if (filmStorage.containsFilm(film)) {
            throw new FilmAlreadyExistException(String.format("Фильм \"%s\" уже добавлен", film.getName()));
        }*/
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
        return filmStorage.getPopularFilms(count);
    }

    @Override
    public List<MPARating> getAllMPA() {
        return mpaStorage.getAllMPARatings();
    }

    @Override
    public MPARating getMPAById(Integer id) {
        return mpaStorage.getMPAById(id);
    }

    @Override
    public List<Genre> getAllGenres() {
        return genreStorage.getAllGenres();
    }

    @Override
    public Genre getGenreById(Integer id) {
        return genreStorage.getGenreById(id);
    }







}
