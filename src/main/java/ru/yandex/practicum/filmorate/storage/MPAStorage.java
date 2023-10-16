package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.MPARating;

import java.util.List;

public interface MPAStorage {

    List<MPARating> getAllMPARatings();

    MPARating getMPAById(Integer id);
}
