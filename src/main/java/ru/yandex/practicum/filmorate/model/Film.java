package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import ru.yandex.practicum.filmorate.validation.NotBeforeCinemaDay;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Jacksonized
@Value
@Builder(toBuilder = true)
public class Film {
    int id;

    @NotBlank(message = "Name may not be blank")
    String name;

    @Size(max = 200)
    String description;

    @NotBeforeCinemaDay
    LocalDate releaseDate;

    @Positive
    int duration;

    @NotNull
    Mpa mpa;

    @Builder.Default
    Set<Genre> genres = new HashSet<>();

    @Builder.Default
    Set<Integer> likes = new HashSet<>();
}
