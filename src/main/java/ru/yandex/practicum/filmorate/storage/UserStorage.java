package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Map;

public interface UserStorage {

    Map<Integer, User> getUsers();

    User getUserById(int id);

    User addUser(User user);

    User updateUser(User user);

    void addFriend(Integer userId, Integer friendId);

    void deleteFriend(Integer userId, Integer friendId);

    Collection<User> getUsersFriends(Integer userId);

    Collection<User> getCommonFriends(Integer userId, Integer otherId);

    boolean containsById(int userId);

    boolean containsUser(User user);
}
