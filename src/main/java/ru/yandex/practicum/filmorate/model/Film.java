package ru.yandex.practicum.filmorate.model;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
@ToString
public class Film {

    private int id;
    @NotNull
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    private LocalDate releaseDate;
    @Positive
    private int duration;
    @AssertTrue
    private boolean dateValidation;
    @NotNull
    private MPARating mpa;
    private Set<Genre> genres;

    public Film(String name, String description, LocalDate releaseDate, int duration, MPARating mpa) {
        this.id = 0;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.dateValidation = releaseDate.isAfter(LocalDate.of(1895,12,28));
        this.mpa = mpa;
        this.genres = new LinkedHashSet<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return duration == film.duration && Objects.equals(name, film.name) && Objects.equals(description, film.description) && Objects.equals(releaseDate, film.releaseDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, releaseDate, duration);
    }
}
