package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.List;

@Value
@RequiredArgsConstructor
@Service
public class GenreService {
    GenreStorage storage;

    public List<Genre> getAll() {
        return storage.getAll();
    }

    public Genre getById(int id) {
        return storage.getById(id);
    }
}
