package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();

    private int userIdentifier = 0;

    @GetMapping("/users")
    public Collection<User> findAll() {
        log.debug("Текущее количество пользователей: {}", users.size());
        return users.values();
    }

    @PostMapping(value = "/users")
    public User create(@RequestBody @Valid User user) {

        if (users.containsValue(user)) {
            throw new ValidationException("Такой пользователь уже существует");
        }

        user.setId(++userIdentifier);

        users.put(user.getId(), user);
        log.debug("Ползователь " + user.getLogin() + " создан, id=" + user.getId());
        return user;
    }

    @PutMapping(value = "/users")
    public User update (@Valid @RequestBody User user) {
        System.out.println("Юзеры которые есть: " + users);
        System.out.println("Обновляется юзер : " + user);

        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Пользоваеля с номером " + user.getId() + " не существует");
        }

        users.put(user.getId(), user);
        log.debug("Пользователь id=" + user.getId() + " обновлен");
        return user;
    }
}
