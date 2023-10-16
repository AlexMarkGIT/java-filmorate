package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPARating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@Primary
public class GenreStorageDB implements GenreStorage {

    JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreStorageDB(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> getAllGenres() {
        String sqlQuery = "select id, name from genres";
        return jdbcTemplate.query(sqlQuery, this::mapRowToGenre);
    }

    @Override
    public void addGenreToFilm(Integer filmId, Integer genreId) {
        String sqlQuery = "insert into film_genre (film_id, genre_id) values (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, genreId);
    }

    @Override
    public void removeGenresFromFilm(Integer filmId) {
        String sqlQuery = "delete from film_genre where film_id = ?";
        jdbcTemplate.update(sqlQuery, filmId);
    }

    @Override
    public Genre getGenreById(Integer id) {
        if (!containsGenreById(id)) {
            throw new NotFoundException("жанр с таким id не найден");
        }
        String sqlQuery = "select id, name from genres where id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToGenre, id);
    }

    @Override
    public List<Genre> getGenresByFilmId(Integer filmId) {
        String sqlQuery = "select g.id, g.name from film_genre as fg " +
                "join genres g on fg.genre_id = g.id " +
                "where fg.film_id = ? " +
                "order by g.id";
        return jdbcTemplate.query(sqlQuery, this::mapRowToGenre, filmId);
    }

    private boolean containsGenreById(Integer id) {
        String sql = "select id from genres where id = ?;";
        SqlRowSet set = jdbcTemplate.queryForRowSet(sql, id);
        return set.next();
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        Genre genre = new Genre(resultSet.getInt("id"));
        genre.setName(resultSet.getString("name"));
        return genre;
    }
}
