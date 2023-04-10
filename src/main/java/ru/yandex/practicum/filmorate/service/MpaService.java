package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.List;

@Value
@RequiredArgsConstructor
@Service
public class MpaService {
    MpaStorage storage;

    public Mpa getById(int id) {
        return storage.getById(id);
    }

    public List<Mpa> getAll() {
        return storage.getAll();
    }
}
