package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

@Repository
@Primary
public class UserStorageDB implements UserStorage {

    JdbcTemplate jdbcTemplate;

    @Autowired
    public UserStorageDB(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getUsers() {
        String sqlQuery = "select id, email, login, name, birthday from users";

        return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }

    @Override
    public User getUserById(Integer id) {
        String sqlQuery = "select id, email, login, name, birthday from users where id = ?";

        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, id);
    }

    @Override
    public User addUser(User user) {

        String sqlQuery = "insert into users(email, login, name, birthday) " +
                "values (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setObject(4, user.getBirthday());
            return stmt;
        }, keyHolder);

        user.setId(keyHolder.getKey().intValue());

        return user;
    }

    @Override
    public User updateUser(User user) {
        String sqlQuery = "update users set email = ?," +
                "login = ?," +
                "name = ?," +
                "birthday = ?" +
                "where id = ?";

        int rowsUpdated = jdbcTemplate.update(sqlQuery, user.getEmail(), user.getLogin(),
                user.getName(), user.getBirthday(), user.getId());

        if (rowsUpdated == 0) {
            throw new UserNotFoundException("Такой пользователь не найден.");
        }

        return user;
    }

    @Override
    public void addFriend(Integer userId, Integer friendId) {
        String sqlQuery = "insert into friends (user_id, friend_id) values (?, ?)";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    @Override
    public void deleteFriend(Integer userId, Integer friendId) {
        String sqlQuery = "delete from friends where user_id = ? and friend_id = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    @Override
    public Collection<User> getUsersFriends(Integer userId) {
        String sqlQuery = "select id, email, login, name, birthday " +
                "from users " +
                "join friends on users.id = friends.friend_id " +
                "where friends.user_id = ?";

        return jdbcTemplate.query(sqlQuery, this::mapRowToUser, userId);
    }

    @Override
    public Collection<User> getCommonFriends(Integer userId, Integer otherId) {

        String sqlQuery = "select users.id, users.email, users.login, users.name, users.birthday from users " +
                "join friends as f1 on users.id = f1.friend_id " +
                "join friends as f2 on f1.friend_id = f2.friend_id " +
                "where f1.user_id = ? and f2.user_id = ? " +
                "order by users.id";

        return jdbcTemplate.query(sqlQuery, this::mapRowToUser, userId, otherId);
    }

    @Override
    public boolean containsById(int userId) {
        String sql = "select id from users where id = ?;";
        SqlRowSet set = jdbcTemplate.queryForRowSet(sql, userId);

        return set.next();
    }

    @Override
    public boolean containsUser(User user) {
        String sql = "select id from users where id = ?;";
        SqlRowSet set = jdbcTemplate.queryForRowSet(sql, user.getId());

        return set.next();
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {

        User user = new User(resultSet.getString("email"),
                resultSet.getString("login"),
                resultSet.getString("name"),
                resultSet.getDate("birthday").toLocalDate());

        user.setId(resultSet.getInt("id"));

        return user;
    }
}
