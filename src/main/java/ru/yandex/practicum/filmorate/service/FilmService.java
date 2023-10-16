package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPARating;

import java.util.Collection;
import java.util.List;

public interface FilmService {

    Collection<Film> findAll();

    Film getFilmById(Integer id);

    Film addFilm(Film film);

    Film updateFilm(Film film);

    void addLike(int filmId, int userId);

    void deleteLike(int filmId, int userId);

    Collection<Film> getPopularFilms(int count);

    List<MPARating> getAllMPA();

    MPARating getMPAById(Integer id);

    List<Genre> getAllGenres();

    Genre getGenreById(Integer id);

}
