package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final HashMap<Integer, User> map = new HashMap<>();
    private AtomicInteger nextId = new AtomicInteger(0);

    @Override
    public List<User> getAll() {
        return new ArrayList<>(map.values());
    }

    @Override
    public User add(User user) {
        User newUser = user.toBuilder()
                .id(nextId.incrementAndGet())
                .build();

        map.put(newUser.getId(), newUser);
        return map.get(newUser.getId());
    }

    @Override
    public User update(User user) {
        if (map.containsKey(user.getId())) {
            map.put(user.getId(), user);
            return map.get(user.getId());
        } else {
            throw new NotFoundException(String.format("User with id=%d not found", user.getId()));
        }
    }

    @Override
    public User getById(int id) {
        if (map.containsKey(id)) {
            return map.get(id);
        } else {
            throw new NotFoundException(String.format("User with id=%d not found", id));
        }
    }

    @Override
    public void clearDB() {
        map.clear();
        nextId = new AtomicInteger(0);
    }
}
