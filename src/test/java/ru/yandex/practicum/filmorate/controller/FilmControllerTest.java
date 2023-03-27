package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.bytebuddy.utility.RandomString;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FilmControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private FilmStorage filmStorage;

    @Autowired
    private UserStorage userStorage;

    @BeforeEach
    void setUp() {
        filmStorage.clearDB();
    }

    @Test
    void handleAdd_PayloadIsValid_ReturnsValidResponseEntity() throws Exception {
        Film newFilm = Film.builder()
                .name("film")
                .description("description of film")
                .releaseDate(LocalDate.of(1999, 1, 1))
                .duration(100).build();

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newFilm)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.name", Matchers.equalTo("film")));
    }

    @Test
    void handleUpdate_PayloadIsValid_ReturnsValidResponseEntity() throws Exception {
        handleAdd_PayloadIsValid_ReturnsValidResponseEntity();

        Film newFilm = Film.builder()
                .id(1)
                .name("new film")
                .description("description of new film")
                .releaseDate(LocalDate.of(1999, 1, 1))
                .duration(200).build();

        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newFilm)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.name", Matchers.equalTo("new film")))
                .andExpect(jsonPath("$.description", Matchers.equalTo("description of new film")))
                .andExpect(jsonPath("$.duration", Matchers.equalTo(200)));
    }

    @Test
    void handleUpdate_IdIsInvalid_ReturnsViolationResponseEntity() throws Exception {
        Film newFilm = Film.builder()
                .id(999)
                .name("film")
                .description("description of film")
                .releaseDate(LocalDate.of(1999, 1, 1))
                .duration(100).build();

        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newFilm)))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    void handleAdd_NameIsNull_ReturnsViolationResponseEntity() throws Exception {
        Film newFilm = Film.builder()
                .description("description of film")
                .releaseDate(LocalDate.of(1999, 1, 1))
                .duration(100).build();

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newFilm)))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.violations[0].fieldName", Matchers.equalTo("name")));
    }

    @Test
    void handleAdd_NameIsEmpty_ReturnsViolationResponseEntity() throws Exception {
        Film newFilm = Film.builder()
                .name("")
                .description("description of film")
                .releaseDate(LocalDate.of(1999, 1, 1))
                .duration(100).build();

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newFilm)))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.violations[0].fieldName", Matchers.equalTo("name")));
    }

    @Test
    void handleAdd_NameIsBlank_ReturnsViolationResponseEntity() throws Exception {
        Film newFilm = Film.builder()
                .name(" ")
                .description("description of film")
                .releaseDate(LocalDate.of(1999, 1, 1))
                .duration(100).build();

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newFilm)))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.violations[0].fieldName", Matchers.equalTo("name")));
    }

    @Test
    void handleAdd_DescriptionIsValid_ReturnsValidResponseEntity() throws Exception {
        Film newFilm = Film.builder()
                .name("film")
                .description(RandomString.make(200))
                .releaseDate(LocalDate.of(1999, 1, 1))
                .duration(100).build();

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newFilm)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.name", Matchers.equalTo("film")));
    }

    @Test
    void handleAdd_DescriptionIsInvalid_ReturnsViolationResponseEntity() throws Exception {
        Film newFilm = Film.builder()
                .name("film")
                .description(RandomString.make(201))
                .releaseDate(LocalDate.of(1999, 1, 1))
                .duration(100).build();

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newFilm)))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.violations[0].fieldName", Matchers.equalTo("description")));
    }

    @Test
    void handleAdd_ReleaseDateIsValid_ReturnsValidResponseEntity() throws Exception {
        Film newFilm = Film.builder()
                .name("new film")
                .description("description of new film")
                .releaseDate(LocalDate.of(1895, 12, 28))
                .duration(100).build();

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newFilm)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.name", Matchers.equalTo("new film")));
    }

    @Test
    void handleAdd_ReleaseDateIsInvalid_ReturnsViolationResponseEntity() throws Exception {
        Film newFilm = Film.builder()
                .name("new film")
                .description("description of new film")
                .releaseDate(LocalDate.of(1895, 12, 27))
                .duration(100).build();

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newFilm)))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.violations[0].fieldName", Matchers.equalTo("releaseDate")));
    }

    @Test
    void handleAdd_DurationIsValid_ReturnsValidResponseEntity() throws Exception {
        Film newFilm = Film.builder()
                .name("new film")
                .description("description of new film")
                .releaseDate(LocalDate.of(1900, 1, 1))
                .duration(1).build();

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newFilm)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.name", Matchers.equalTo("new film")));
    }

    @Test
    void handleAdd_DurationIsInvalid_ReturnsViolationResponseEntity() throws Exception {
        Film newFilm = Film.builder()
                .name("new film")
                .description("description of new film")
                .releaseDate(LocalDate.of(1900, 1, 1))
                .duration(0).build();

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newFilm)))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.violations[0].fieldName", Matchers.equalTo("duration")));
    }

    @Test
    void handleGetById_IdIsValid_ReturnsValidResponseEntity() throws Exception {
        Film newFilm = Film.builder()
                .name("film")
                .description("description of film")
                .releaseDate(LocalDate.of(1999, 1, 1))
                .duration(100).build();
        filmStorage.add(newFilm);

        mockMvc.perform(get("/films/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.name", Matchers.equalTo("film")));
    }

    @Test
    void handleGetById_IdIsInvalid_ReturnsNotFoundStatus() throws Exception {
        Film newFilm = Film.builder()
                .name("film")
                .description("description of film")
                .releaseDate(LocalDate.of(1999, 1, 1))
                .duration(100).build();
        filmStorage.add(newFilm);

        mockMvc.perform(get("/films/999"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void handlePutLike_IdsAreValid_ReturnsTrue() throws Exception {
        Film newFilm = Film.builder()
                .name("film")
                .description("description of film")
                .releaseDate(LocalDate.of(1999, 1, 1))
                .duration(100).build();
        filmStorage.add(newFilm);

        User newUser = User.builder()
                .name("user")
                .login("login")
                .email("new@user.com")
                .birthday(LocalDate.of(1999, 1, 1)).build();
        userStorage.add(newUser);

        mockMvc.perform(put("/films/1/like/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.equalTo(true)));
    }

    @Test
    void handlePutLike_IdFilmIsInvalid_ReturnsNotFoundStatus() throws Exception {
        Film newFilm = Film.builder()
                .name("film")
                .description("description of film")
                .releaseDate(LocalDate.of(1999, 1, 1))
                .duration(100).build();
        filmStorage.add(newFilm);

        User newUser = User.builder()
                .name("user")
                .login("login")
                .email("new@user.com")
                .birthday(LocalDate.of(1999, 1, 1)).build();
        userStorage.add(newUser);

        mockMvc.perform(put("/films/999/like/1"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void handlePutLike_IdUserIsInvalid_ReturnsNotFoundStatus() throws Exception {
        Film newFilm = Film.builder()
                .name("film")
                .description("description of film")
                .releaseDate(LocalDate.of(1999, 1, 1))
                .duration(100).build();
        filmStorage.add(newFilm);

        User newUser = User.builder()
                .name("user")
                .login("login")
                .email("new@user.com")
                .birthday(LocalDate.of(1999, 1, 1)).build();
        userStorage.add(newUser);

        mockMvc.perform(put("/films/1/like/999"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void handleDeleteLike_IdsAreValid_ReturnsTrue() throws Exception {
        Film newFilm = Film.builder()
                .name("film")
                .description("description of film")
                .releaseDate(LocalDate.of(1999, 1, 1))
                .duration(100).build();
        filmStorage.add(newFilm);

        User newUser = User.builder()
                .name("user")
                .login("login")
                .email("new@user.com")
                .birthday(LocalDate.of(1999, 1, 1)).build();
        userStorage.add(newUser);

        mockMvc.perform(put("/films/1/like/1"));

        mockMvc.perform(delete("/films/1/like/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.equalTo(true)));
    }

    @Test
    void handleDeleteLike_IdFilmIsInvalid_ReturnsNotFoundStatus() throws Exception {
        Film newFilm = Film.builder()
                .name("film")
                .description("description of film")
                .releaseDate(LocalDate.of(1999, 1, 1))
                .duration(100).build();
        filmStorage.add(newFilm);

        User newUser = User.builder()
                .name("user")
                .login("login")
                .email("new@user.com")
                .birthday(LocalDate.of(1999, 1, 1)).build();
        userStorage.add(newUser);

        mockMvc.perform(put("/films/1/like/1"));

        mockMvc.perform(put("/films/999/like/1"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void handleDeleteLike_IdUserIsInvalid_ReturnsNotFoundStatus() throws Exception {
        Film newFilm = Film.builder()
                .name("film")
                .description("description of film")
                .releaseDate(LocalDate.of(1999, 1, 1))
                .duration(100).build();
        filmStorage.add(newFilm);

        User newUser = User.builder()
                .name("user")
                .login("login")
                .email("new@user.com")
                .birthday(LocalDate.of(1999, 1, 1)).build();
        userStorage.add(newUser);

        mockMvc.perform(put("/films/1/like/1"));

        mockMvc.perform(put("/films/1/like/999"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}