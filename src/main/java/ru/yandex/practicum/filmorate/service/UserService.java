package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Value
@RequiredArgsConstructor
@Service
public class UserService {
    @Qualifier("userDbStorage")
    UserStorage storage;

    public List<User> getAll() {
        return storage.getAll();
    }

    public User add(User user) {
        return storage.add(user);
    }

    public User update(User user) {
        return storage.update(user);
    }

    public User getById(int id) {
        return storage.getById(id);
    }

    public List<User> getCommonFriends(int id, int otherId) {
        Set<Integer> friends = new HashSet<>(storage.getById(id).getFriends());
        friends.retainAll(storage.getById(otherId).getFriends());
        return friends.stream()
                .map(storage::getById)
                .collect(Collectors.toList());
    }

    public boolean addFriend(int id, int friendId) {
        User user = storage.getById(id);
        User friend = storage.getById(friendId);
        boolean isAddOk = user.getFriends().add(friend.getId());
        if (isAddOk) {
            storage.update(user);
        }
        return isAddOk;
    }

    public boolean deleteFriend(int id, int friendId) {
        User user = storage.getById(id);
        User friend = storage.getById(friendId);
        boolean isDelOk = user.getFriends().remove(friend.getId());
        if (isDelOk) {
            storage.update(user);
            friend.getFriends().remove(user.getId());
            storage.update(friend);
        }
        return isDelOk;
    }

    public List<User> getFriends(int id) {
        return storage.getById(id).getFriends().stream()
                .map(storage::getById)
                .collect(Collectors.toList());
    }
}

