package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.ValidationException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final HashMap<Integer, User> map = new HashMap<>();
    private AtomicInteger nextId = new AtomicInteger(0);

    public void clearDB() {
        map.clear();
        nextId = new AtomicInteger(0);
    }

    @GetMapping
    public List<User> getAll() {
        return new ArrayList<>(map.values());
    }

    @PostMapping
    public User add(@Valid @RequestBody User user) {
        log.info("Adding user: {}", user);

        User newUser = user.toBuilder()
                .id(nextId.incrementAndGet())
                .build();
        map.put(newUser.getId(), newUser);
        return map.get(newUser.getId());
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        if (map.containsKey(user.getId())) {
            log.info("Updating user: {}", user);

            map.put(user.getId(), user);
            return map.get(user.getId());
        } else {
            throw new ValidationException("id", String.format("User with id=%d not found", user.getId()));
        }
    }
}
