package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Value
@Builder(toBuilder = true)
public class User {
    int id;

    @NotBlank
    @Email
    String email;

    @NotBlank
    @Pattern(regexp = "^\\w+$")
    String login;

    String name;

    @Past
    LocalDate birthday;

    Set<Integer> friends;

    @Builder
    public User(int id, String email, String login, String name, LocalDate birthday, Set<Integer> friends) {
        this.id = id;
        this.email = email;
        this.login = login;
        if (name == null || name.isBlank()) {
            this.name = login;
        } else {
            this.name = name;
        }
        this.birthday = birthday;
        this.friends = Objects.requireNonNullElseGet(friends, HashSet::new);
    }
}
