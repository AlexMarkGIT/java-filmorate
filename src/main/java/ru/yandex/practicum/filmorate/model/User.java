package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Getter
@Setter
@ToString
public class User {

    @Min(0)
    private int id;
    @Email
    private String email;
    @NotNull
    @NotBlank
    private String login;
    private String name;
    @Past
    private LocalDate birthday;
    @AssertTrue
    private boolean loginValidation;
    private List<Integer> friends = new ArrayList<>();


    public User(String email, String login, String name, LocalDate birthday) {
        this.id = 0;
        this.email = email;
        this.login = login;
        if (name == null || name.isBlank()) this.name = login;
        else this.name = name;
        this.birthday = birthday;
        loginValidation = !login.contains(" ");
    }

    public void addFriend(Integer friendId) {
        friends.add(friendId);
    }

    public void deleteFriend(Integer friendId) {
        friends.remove(friendId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.email) && Objects.equals(login, user.login) && Objects.equals(name, user.name) && Objects.equals(birthday, user.birthday);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, login, name, birthday);
    }
}
