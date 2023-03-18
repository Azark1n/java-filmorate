package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> map = new HashMap<>();
    private AtomicInteger nextId = new AtomicInteger(0);

    public void clearDB() {
        map.clear();
        nextId = new AtomicInteger(0);
    }

    @GetMapping
    public List<Film> getAll() {
        return new ArrayList<>(map.values());
    }

    @PostMapping
    public Film add(@Valid @RequestBody Film film) {
        Film newFilm = film.toBuilder()
                .id(nextId.incrementAndGet())
                .build();

        log.info("Adding film: {}", newFilm);

        map.put(newFilm.getId(), newFilm);
        return map.get(newFilm.getId());
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        if (map.containsKey(film.getId())) {
            log.info("Updating film: {}", film);

            map.put(film.getId(), film);
            return map.get(film.getId());
        } else {
            throw new NotFoundException(String.format("Film with id=%d not found", film.getId()));
        }
    }
}
