package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@Value
@RequiredArgsConstructor
@RestController
@RequestMapping("/mpa")
public class MpaController {
    MpaService service;

    @GetMapping
    public List<Mpa> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Mpa getById(@PathVariable int id) {
        return service.getById(id);
    }
}
