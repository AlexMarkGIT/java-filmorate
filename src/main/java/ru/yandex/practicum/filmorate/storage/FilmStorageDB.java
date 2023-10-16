package ru.yandex.practicum.filmorate.storage;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPARating;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Repository
@Primary
public class FilmStorageDB implements FilmStorage {

    JdbcTemplate jdbcTemplate;
    GenreStorage genreStorage;

    @Autowired
    public FilmStorageDB(JdbcTemplate jdbcTemplate, GenreStorageDB genreStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreStorage = genreStorage;
    }

    @Override
    public List<Film> getFilms() {

        String sqlQuery = "select id, " +
                "name, " +
                "description, " +
                "release_date, " +
                "duration, " +
                "rating_id " +
                "from films";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        String sqlQuery = "select f.id, " +
                "f.name, " +
                "f.description, " +
                "f.release_date, " +
                "f.duration, " +
                "f.rating_id, " +
                "count(l.user_id) as likes_count " +
                "from films as f " +
                "left join likes as l on f.id = l.film_id " +
                "group by f.id " +
                "order by likes_count desc " +
                "limit ?";

        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm, count);
    }

    @Override
    public Film getFilmById(int id) {
        String sqlQuery = "select id, " +
                "name, " +
                "description, " +
                "release_date, " +
                "duration, " +
                "rating_id " +
                "from films where id = ?";

        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, id);
    }

    @Override
    public Film addFilm(Film film) {
        String sqlQuery = "insert into films (name, description, release_date, duration, rating_id) " +
                "values (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setObject(3, film.getReleaseDate());
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);

        film.setId(keyHolder.getKey().intValue());

        if (film.getGenres().size() != 0) {
            for (Genre genre : film.getGenres()) {
                genreStorage.addGenreToFilm(film.getId(), genre.getId());
            }
        }

        return film;
    }

    @Override
    public Film updateFilm(Film film) {

        String sqlQuery = "update films set name = ?, " +
                "description = ?, " +
                "release_date = ?, " +
                "duration = ?, " +
                "rating_id = ? " +
                "where id = ?";

        int rowsUpdated = jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());

        if (rowsUpdated != 0) {
            genreStorage.removeGenresFromFilm(film.getId());
            for (Genre genre : film.getGenres()) {
                genreStorage.addGenreToFilm(film.getId(), genre.getId());
            }
        }

        Set<Genre> genres = new LinkedHashSet<>(genreStorage.getGenresByFilmId(film.getId()));
        film.setGenres(genres);

        return film;
    }

    @Override
    public boolean containsById(int filmId) {
        String sql = "select id from films where id = ?";
        SqlRowSet set = jdbcTemplate.queryForRowSet(sql, filmId);

        return set.next();
    }

    @Override
    public boolean containsFilm(Film film) {
        String sql = "select id from films where id = ?";
        SqlRowSet set = jdbcTemplate.queryForRowSet(sql, film.getId());

        return set.next();
    }

    @Override
    public void addLike(int filmId, int userId) {
        String sqlQuery = "insert into likes (film_id, user_id) " +
                "values (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public void deleteLike(int filmId, int userId) {
        String sqlQuery = "delete from likes where film_id = ? and user_id = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {

        MPARating mpa = new MPARating(resultSet.getInt("rating_id"));

        Film film = new Film(resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getDate("release_date").toLocalDate(),
                resultSet.getInt("duration"),
                mpa);

        film.setId(resultSet.getInt("id"));

        Set<Genre> genres = new LinkedHashSet<>(genreStorage.getGenresByFilmId(film.getId()));
        film.setGenres(genres);

        return film;
    }

}
