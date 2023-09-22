package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage implements UserStorage {

    Map<Integer, User> users = new HashMap<>();

    private int globalUserId = 0;

    public Map<Integer, User> getUsers() {
        return users;
    }

    public User getUserById(int id) {
        return users.get(id);
    }

    public User addUser(User user) {
        user.setId(++globalUserId);
        users.put(user.getId(), user);
        return user;
    }

    public User updateUser(User user) {
        users.put(user.getId(), user);
        return user;
    }

    public void addFriend(Integer userId, Integer friendId) {
        User user = users.get(userId);
        user.addFriend(friendId);
        users.put(user.getId(), user);

        User friend = users.get(friendId);
        friend.addFriend(userId);
        users.put(friend.getId(), friend);
    }

    public void deleteFriend(Integer userId, Integer friendId) {
        User user = users.get(userId);
        user.deleteFriend(friendId);
        users.put(user.getId(), user);

        User friend = users.get(friendId);
        friend.deleteFriend(userId);
        users.put(friend.getId(), friend);
    }

    public Collection<User> getUsersFriends(Integer userId) {
        List<User> friends = new ArrayList<>();
        for (Integer friendId : users.get(userId).getFriends()) {
            friends.add(users.get(friendId));
        }

        return friends;
    }

    public Collection<User> getCommonFriends(Integer userId, Integer otherId) {
        return getUsersFriends(userId).stream()
                .filter(f -> getUsersFriends(otherId).contains(f))
                .collect(Collectors.toList());
    }

    public boolean containsById(int userId) {
        return users.containsKey(userId);
    }

    public boolean containsUser(User user) {
        return users.containsValue(user);
    }

}
