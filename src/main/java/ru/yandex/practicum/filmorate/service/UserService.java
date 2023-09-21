package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserService {

    Collection<User> findAllUsers();

    User addUser(User user);

    User updateUser(User user);

    User getUserById(Integer id);

    void addFiend(Integer userId, Integer friendId);

    void deleteFriend(Integer userId, Integer friendId);

    Collection<User> getUsersFriend(Integer userId);

    Collection<User> getCommonFriend(Integer userId, Integer otherId);

    boolean containsById(int userId);
}
