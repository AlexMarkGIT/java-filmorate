package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorageDB;

import java.util.Collection;

@Service
public class UserServiceDefault implements UserService {

    UserStorage userStorage;

    public UserServiceDefault(UserStorageDB userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> findAllUsers() {
        return userStorage.getUsers();
    }

    public User addUser(User user) {
        if (userStorage.containsUser(user)) {
            throw new UserAlreadyExistException((String.format("Пользователь \"%s\" уже добавлен", user.getLogin())));
        }
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        if (!userStorage.containsById(user.getId())) {
            throw new UserNotFoundException((String.format("Пользователь с id \"%d\" не найден", user.getId())));
        }
        return userStorage.updateUser(user);
    }

    public User getUserById(Integer id) {
        if (!userStorage.containsById(id)) {
            throw new UserNotFoundException(String.format("Пользователь с id \"%d\" не найден", id));
        }
        return userStorage.getUserById(id);
    }

    public void addFiend(Integer userId, Integer friendId) {
        if (!userStorage.containsById(userId)) {
            throw new UserNotFoundException(String.format("Пользователь с id \"%d\" не найден", userId));
        }
        if (!userStorage.containsById(friendId)) {
            throw new UserNotFoundException(String.format("Пользователь с id \"%d\" не найден", friendId));
        }
        userStorage.addFriend(userId, friendId);
    }

    public void deleteFriend(Integer userId, Integer friendId) {
        if (!userStorage.containsById(userId)) {
            throw new UserNotFoundException(String.format("Пользователь с id \"%d\" не найден", userId));
        }
        if (!userStorage.containsById(friendId)) {
            throw new UserNotFoundException(String.format("Пользователь с id \"%d\" не найден", friendId));
        }
        userStorage.deleteFriend(userId, friendId);
    }

    public Collection<User> getUsersFriend(Integer userId) {
        if (!userStorage.containsById(userId)) {
            throw new UserNotFoundException(String.format("Пользователь с id \"%d\" не найден", userId));
        }
        return userStorage.getUsersFriends(userId);
    }

    public Collection<User> getCommonFriend(Integer userId, Integer otherId) {
        if (!userStorage.containsById(userId)) {
            throw new UserNotFoundException(String.format("Пользователь с id \"%d\" не найден", userId));
        }
        if (!userStorage.containsById(otherId)) {
            throw new UserNotFoundException(String.format("Пользователь с id \"%d\" не найден", otherId));
        }
        return userStorage.getCommonFriends(userId, otherId);
    }

    public boolean containsById(int userId) {
        return userStorage.containsById(userId);
    }
}
