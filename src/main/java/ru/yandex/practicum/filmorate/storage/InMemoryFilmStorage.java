package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    // пока что не стал удалять этот класс

    private final Map<Integer, Film> films = new HashMap<>();
    private int globalFilmId = 0;

    public List<Film> getFilms() {
        return null;
    }

    public Film getFilmById(int id) {
        return films.get(id);
    }

    public Film addFilm(Film film) {
        film.setId(++globalFilmId);
        films.put(film.getId(), film);
        return film;
    }

    public Film updateFilm(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    public boolean containsById(int filmId) {
        return films.containsKey(filmId);
    }

    public boolean containsFilm(Film film) {
        return films.containsValue(film);
    }

    public void addLike(int filmId, int userId) {
        Film film = films.get(filmId);
        //film.addLike(userId);
        films.put(film.getId(), film);
    }

    public void deleteLike(int filmId, int userId) {
        Film film = films.get(filmId);
        //ilm.deleteLike(userId);
        films.put(film.getId(), film);
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        return null;
    }
}
