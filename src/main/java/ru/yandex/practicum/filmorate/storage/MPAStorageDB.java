package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MPARating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@Primary
public class MPAStorageDB implements MPAStorage {

    JdbcTemplate jdbcTemplate;

    @Autowired
    public MPAStorageDB(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<MPARating> getAllMPARatings() {
        String sqlQuery = "select id from mpa";
        return jdbcTemplate.query(sqlQuery, this::mapRowToMPA);
    }

    @Override
    public MPARating getMPAById(Integer id) {
        if (!containsMPAById(id)) {
            throw new NotFoundException("рейтинг с таким id не найден");
        }
        String sqlQuery = "select id from mpa where id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToMPA, id);
    }

    private boolean containsMPAById(Integer MPAid) {
        String sql = "select id from mpa where id = ?;";
        SqlRowSet set = jdbcTemplate.queryForRowSet(sql, MPAid);

        return set.next();
    }

    private MPARating mapRowToMPA(ResultSet resultSet, int rowNum) throws SQLException {
        return new MPARating(resultSet.getInt("id"));
    }
}
