package ru.yandex.practicum.filmorate.storage.film.impl;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> map = new HashMap<>();
    private AtomicInteger nextId = new AtomicInteger(0);

    @Override
    public Film add(Film film) {
        Film newFilm = film.toBuilder()
                .id(nextId.incrementAndGet())
                .build();

        map.put(newFilm.getId(), newFilm);
        return map.get(newFilm.getId());
    }

    @Override
    public Film update(Film film) {
        if (map.containsKey(film.getId())) {
            map.put(film.getId(), film);
            return map.get(film.getId());
        } else {
            throw new NotFoundException(String.format("Film with id=%d not found", film.getId()));
        }
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(map.values());
    }

    @Override
    public Film getById(int id) {
        if (map.containsKey(id)) {
            return map.get(id);
        } else {
            throw new NotFoundException(String.format("Film with id=%d not found", id));
        }
    }

    @Override
    public void clearDB() {
        map.clear();
        nextId = new AtomicInteger(0);
    }
}
