package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {

    List<Genre> getAllGenres();
    Genre getGenreById(Integer id);
    void addGenreToFilm(Integer filmId, Integer genreId);
    void removeGenresFromFilm(Integer filmId);
    List<Genre> getGenresByFilmId(Integer filmId);

}
