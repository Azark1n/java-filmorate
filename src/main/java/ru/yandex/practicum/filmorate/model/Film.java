package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Value;
import ru.yandex.practicum.filmorate.validation.NotBeforeCinemaDay;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

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
}
