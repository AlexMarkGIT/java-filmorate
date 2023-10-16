package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MPARating {
    private Integer id;
    private String name;

    public MPARating(Integer id) {
        this.id = id;
        this.name = getMPANameById(id);
    }

    private String getMPANameById(Integer id) {
        String[] ratingNames = {"G", "PG", "PG-13", "R", "NC-17"};
        return ratingNames[id - 1];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MPARating mpaRating = (MPARating) o;
        return id.equals(mpaRating.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
