package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Value
@RequiredArgsConstructor
@Service
public class FilmService {
    @Qualifier("filmDbStorage")
    FilmStorage storage;
    @Qualifier("userDbStorage")
    UserStorage userStorage;

    public List<Film> getAll() {
        return storage.getAll();
    }

    public Film getById(int id) {
        return storage.getById(id);
    }

    public Film add(Film film) {
        return storage.add(film);
    }

    public Film update(Film film) {
        return storage.update(film);
    }

    public boolean putLike(int id, int userId) {
        userStorage.getById(userId);
        Film film = storage.getById(id);
        boolean isAdded = film.getLikes().add(userId);
        if (isAdded) {
            storage.update(film);
        }
        return isAdded;
    }

    public boolean deleteLike(int id, int userId) {
        userStorage.getById(userId);
        Film film = storage.getById(id);
        boolean isRemoved = film.getLikes().remove(userId);
        if (isRemoved) {
            storage.update(film);
        }
        return isRemoved;
    }

    public List<Film> getPopular(int count) {
        return storage.getAll().stream()
                .sorted(Comparator.comparingInt(f -> f.getLikes().size() * (-1)))
                .limit(count)
                .collect(Collectors.toList());
    }
}
