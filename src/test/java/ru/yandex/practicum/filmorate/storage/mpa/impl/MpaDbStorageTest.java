package ru.yandex.practicum.filmorate.storage.mpa.impl;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@Value
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class MpaDbStorageTest {
    MpaDbStorage mpaDbStorage;

    @Test
    void testGetAll() {
        List<Mpa> mpaList = List.of(Mpa.builder().id(1).name("G").build(),
                Mpa.builder().id(2).name("PG").build(),
                Mpa.builder().id(3).name("PG-13").build(),
                Mpa.builder().id(4).name("R").build(),
                Mpa.builder().id(5).name("NC-17").build());

        List<Mpa> allMpa = mpaDbStorage.getAll();

        AssertionsForInterfaceTypes.assertThat(allMpa).containsAll(mpaList);
    }

    @Test
    void testGetById() {
        Mpa mpaById = mpaDbStorage.getById(1);

        assertThat(mpaById)
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("name", "G");
    }
}