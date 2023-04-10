package ru.yandex.practicum.filmorate.storage.film.impl;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@Value
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTest {
    FilmDbStorage filmDbStorage;

    private static Film film1;
    private static Film currentFilm;

    @BeforeAll
    static void beforeAll() {
        film1 = Film.builder()
                .name("film1")
                .description("description of film1")
                .releaseDate(LocalDate.of(1999, 1, 1))
                .mpa(Mpa.builder().id(1).build())
                .duration(100).build();
    }

    @Test
    void testAdd() {
        currentFilm = filmDbStorage.add(film1);

        assertThat(currentFilm)
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("name", "film1");
    }

    @Test
    void testUpdate() {
        Mpa newMpa = Mpa.builder().id(2).name("PG").build();
        Film updatedFilm = currentFilm.toBuilder()
                .name("updated film")
                .duration(200)
                .mpa(newMpa)
                .build();

        currentFilm = filmDbStorage.update(updatedFilm);

        assertThat(currentFilm)
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("name", "updated film")
                .hasFieldOrPropertyWithValue("duration", 200)
                .hasFieldOrPropertyWithValue("mpa", newMpa);

    }

    @Test
    void testGetAll() {
        List<Film> allFilms = filmDbStorage.getAll();

        assertThat(allFilms).containsExactly(currentFilm);
    }

    @Test
    void testGetById() {
        Film filmById = filmDbStorage.getById(1);

        assertThat(filmById).isEqualTo(currentFilm);
    }
}