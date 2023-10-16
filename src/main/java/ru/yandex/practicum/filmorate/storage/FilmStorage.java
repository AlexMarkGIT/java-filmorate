package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    List<Film> getFilms();

    Film getFilmById(int id);

    Film addFilm(Film film);

    Film updateFilm(Film film);

    boolean containsById(int filmId);

    boolean containsFilm(Film film);

    void addLike(int filmId, int userId);

    void deleteLike(int filmId, int userId);

    List<Film> getPopularFilms(Integer count);
}
