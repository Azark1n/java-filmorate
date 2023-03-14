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

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
    private FilmController filmController;

    @BeforeEach
    void setUp() {
        filmController.clearDB();
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
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.violations[0].fieldName", Matchers.equalTo("id")));
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
}