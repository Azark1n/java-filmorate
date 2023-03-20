package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private UserController userController;

    @BeforeEach
    void setUp() {
        userController.clearDB();
    }

    @Test
    void handleAdd_PayloadIsValid_ReturnsValidResponseEntity() throws Exception {
        User newUser = User.builder()
                .name("user")
                .login("login")
                .email("new@user.com")
                .birthday(LocalDate.of(1999, 1, 1)).build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newUser)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.name", Matchers.equalTo("user")));
    }

    @Test
    void handleUpdate_PayloadIsValid_ReturnsValidResponseEntity() throws Exception {
        handleAdd_PayloadIsValid_ReturnsValidResponseEntity();

        User newUser = User.builder()
                .id(1)
                .name("newUser")
                .login("login")
                .email("new@user.com")
                .birthday(LocalDate.of(1999, 1, 1)).build();

        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newUser)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.name", Matchers.equalTo("newUser")));
    }

    @Test
    void handleUpdate_IdIsInvalid_ReturnsViolationResponseEntity() throws Exception {
        User newUser = User.builder()
                .id(999)
                .name("newUser")
                .login("login")
                .email("new@user.com")
                .birthday(LocalDate.of(1999, 1, 1)).build();

        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newUser)))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    void handleAdd_NameIsNull_ReturnsValidResponseEntity() throws Exception {
        User newUser = User.builder()
                .login("login")
                .email("new@user.com")
                .birthday(LocalDate.of(1999, 1, 1)).build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newUser)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.name", Matchers.equalTo("login")));
    }

    @Test
    void handleAdd_NameIsEmpty_ReturnsValidResponseEntity() throws Exception {
        User newUser = User.builder()
                .name("")
                .login("login")
                .email("new@user.com")
                .birthday(LocalDate.of(1999, 1, 1)).build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newUser)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.name", Matchers.equalTo("login")));
    }

    @Test
    void handleAdd_NameIsBlank_ReturnsValidResponseEntity() throws Exception {
        User newUser = User.builder()
                .name(" ")
                .login("login")
                .email("new@user.com")
                .birthday(LocalDate.of(1999, 1, 1)).build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newUser)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.name", Matchers.equalTo("login")));
    }

    @Test
    void handleAdd_LoginIsNull_ReturnsViolationResponseEntity() throws Exception {
        User newUser = User.builder()
                .name("user")
                .email("new@user.com")
                .birthday(LocalDate.of(1999, 1, 1)).build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newUser)))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.violations[0].fieldName", Matchers.equalTo("login")));
    }

    @Test
    void handleAdd_LoginIsEmpty_ReturnsViolationResponseEntity() throws Exception {
        User newUser = User.builder()
                .name("user")
                .login("")
                .email("new@user.com")
                .birthday(LocalDate.of(1999, 1, 1)).build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newUser)))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.violations[0].fieldName", Matchers.equalTo("login")));
    }

    @Test
    void handleAdd_LoginIsBlank_ReturnsViolationResponseEntity() throws Exception {
        User newUser = User.builder()
                .name("user")
                .login(" ")
                .email("new@user.com")
                .birthday(LocalDate.of(1999, 1, 1)).build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newUser)))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.violations[0].fieldName", Matchers.equalTo("login")));
    }

    @Test
    void handleAdd_LoginIsInvalid_ReturnsViolationResponseEntity() throws Exception {
        User newUser = User.builder()
                .name("user")
                .login("lo gin")
                .email("new@user.com")
                .birthday(LocalDate.of(1999, 1, 1)).build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newUser)))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.violations[0].fieldName", Matchers.equalTo("login")));
    }

    @Test
    void handleAdd_EmailIsNull_ReturnsViolationResponseEntity() throws Exception {
        User newUser = User.builder()
                .name("user")
                .login("login")
                .birthday(LocalDate.of(1999, 1, 1)).build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newUser)))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.violations[0].fieldName", Matchers.equalTo("email")));
    }

    @Test
    void handleAdd_EmailIsEmpty_ReturnsViolationResponseEntity() throws Exception {
        User newUser = User.builder()
                .name("user")
                .login("login")
                .email("")
                .birthday(LocalDate.of(1999, 1, 1)).build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newUser)))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.violations[0].fieldName", Matchers.equalTo("email")));
    }

    @Test
    void handleAdd_EmailIsBlank_ReturnsViolationResponseEntity() throws Exception {
        User newUser = User.builder()
                .name("user")
                .login("login")
                .email(" ")
                .birthday(LocalDate.of(1999, 1, 1)).build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newUser)))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.violations[0].fieldName", Matchers.equalTo("email")));
    }

    @Test
    void handleAdd_EmailIsInvalid_ReturnsViolationResponseEntity() throws Exception {
        User newUser = User.builder()
                .name("user")
                .login("login")
                .email("email.com")
                .birthday(LocalDate.of(1999, 1, 1)).build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newUser)))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.violations[0].fieldName", Matchers.equalTo("email")));
    }

    @Test
    void handleAdd_BirthdayIsNow_ReturnsViolationResponseEntity() throws Exception {
        User newUser = User.builder()
                .login("login")
                .name("user")
                .email("new@user.com")
                .birthday(LocalDate.now()).build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newUser)))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.violations[0].fieldName", Matchers.equalTo("birthday")));
    }

    @Test
    void handleAdd_BirthdayIsYesterday_ReturnsValidResponseEntity() throws Exception {
        User newUser = User.builder()
                .login("login")
                .name("user")
                .email("new@user.com")
                .birthday(LocalDate.now()).build();
        User newUser2 = newUser.toBuilder().birthday(LocalDate.now().minusDays(1)).build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newUser2)))
                .andDo(print())
                .andExpect(status().isOk());
    }
}