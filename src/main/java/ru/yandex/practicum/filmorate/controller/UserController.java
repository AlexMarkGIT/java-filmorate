package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.service.UserServiceImpl;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@Slf4j
public class UserController {

    private final UserService userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public Collection<User> findAll() {
        log.debug("Текущее количество пользователей: {}", userService.findAllUsers().size());
        return userService.findAllUsers();
    }

    @PostMapping(value = "/users")
    public User create(@RequestBody @Valid User user) {
        log.debug("Ползователь " + user.getLogin() + " создан");
        return userService.addUser(user);
    }

    @PutMapping(value = "/users")
    public User update(@Valid @RequestBody User user) {
        log.debug("Пользователь id=" + user.getId() + " обновлен");
        return userService.updateUser(user);
    }

    @GetMapping(value = "/users/{id}")
    public User getUserById(@PathVariable("id") Integer userId) {
        return userService.getUserById(userId);
    }

    @PutMapping(value = "/users/{userId}/friends/{friendId}")
    public void addFriend(@PathVariable("userId") Integer userId,
                          @PathVariable("friendId") Integer friendId) {
        userService.addFiend(userId, friendId);
    }

    @DeleteMapping(value = "/users/{userId}/friends/{friendId}")
    public void deleteFriend(@PathVariable("userId") Integer userId,
                          @PathVariable("friendId") Integer friendId) {
        userService.deleteFriend(userId, friendId);
    }

    @GetMapping(value = "/users/{userId}/friends")
    public Collection<User> getUsersFriends(@PathVariable("userId") Integer userId) {
        return userService.getUsersFriend(userId);
    }

    @GetMapping(value = "/users/{userId}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable("userId") Integer userId,
                                            @PathVariable("otherId") Integer otherId) {
        return userService.getCommonFriend(userId,otherId);
    }

}
