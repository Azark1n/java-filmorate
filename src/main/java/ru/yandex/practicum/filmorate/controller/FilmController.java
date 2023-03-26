package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService service;

    public FilmController(FilmService service) {
        this.service = service;
    }

    @GetMapping
    public List<Film> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Film getById(@PathVariable int id) {
        return service.getById(id);
    }


    @PostMapping
    public Film add(@Valid @RequestBody Film film) {
        log.info("Adding film: {}", film);
        return service.add(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Updating film: {}", film);
        return service.update(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public boolean putLike(@PathVariable int id, @PathVariable int userId) {
        return service.putLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public boolean deleteLike(@PathVariable int id, @PathVariable int userId) {
        return service.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(defaultValue = "10") int count) {
        return service.getPopular(count);
    }
}
