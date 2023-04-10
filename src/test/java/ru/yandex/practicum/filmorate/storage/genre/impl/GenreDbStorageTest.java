package ru.yandex.practicum.filmorate.storage.genre.impl;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@Value
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GenreDbStorageTest {
    GenreDbStorage genreDbStorage;

    @Test
    void testGetAll() {
        List<Genre> genres = List.of(Genre.builder().id(1).name("Комедия").build(),
                Genre.builder().id(2).name("Драма").build(),
                Genre.builder().id(3).name("Мультфильм").build(),
                Genre.builder().id(4).name("Триллер").build(),
                Genre.builder().id(5).name("Документальный").build(),
                Genre.builder().id(6).name("Боевик").build());

        List<Genre> allGenres = genreDbStorage.getAll();

        AssertionsForInterfaceTypes.assertThat(allGenres).containsAll(genres);
    }

    @Test
    void testGetById() {
        Genre genreById = genreDbStorage.getById(1);

        assertThat(genreById)
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("name", "Комедия");
    }
}