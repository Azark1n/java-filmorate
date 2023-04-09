package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    @Qualifier("userDbStorage")
    private UserStorage userStorage;

    @BeforeEach
    void setUp() {
        userStorage.clearDB();
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

    @Test
    void handleGetById_IdIsValid_ReturnsValidResponseEntity() throws Exception {
        User newUser = User.builder()
                .name("user")
                .login("login")
                .email("new@user.com")
                .birthday(LocalDate.of(1999, 1, 1)).build();
        userStorage.add(newUser);

        mockMvc.perform(get("/users/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.name", Matchers.equalTo("user")));
    }

    @Test
    void handleGetById_IdIsInvalid_ReturnsNotFoundStatus() throws Exception {
        User newUser = User.builder()
                .name("user")
                .login("login")
                .email("new@user.com")
                .birthday(LocalDate.of(1999, 1, 1)).build();
        userStorage.add(newUser);

        mockMvc.perform(get("/users/999"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void handleAddFriend_IdsAreValid_ReturnsTrue() throws Exception {
        User newUser1 = User.builder()
                .name("user")
                .login("login")
                .email("new@user.com")
                .birthday(LocalDate.of(1999, 1, 1)).build();
        userStorage.add(newUser1);

        User newUser2 = User.builder()
                .name("user2")
                .login("login2")
                .email("new2@user.com")
                .birthday(LocalDate.of(1999, 1, 1)).build();
        userStorage.add(newUser2);

        mockMvc.perform(put("/users/1/friends/2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.equalTo(true)));
    }

    @Test
    void handleAddFriend_IdUser1IsInvalid_ReturnsNotFoundStatus() throws Exception {
        User newUser1 = User.builder()
                .name("user")
                .login("login")
                .email("new@user.com")
                .birthday(LocalDate.of(1999, 1, 1)).build();
        userStorage.add(newUser1);

        User newUser2 = User.builder()
                .name("user2")
                .login("login2")
                .email("new2@user.com")
                .birthday(LocalDate.of(1999, 1, 1)).build();
        userStorage.add(newUser2);

        mockMvc.perform(put("/users/999/friends/2"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void handleAddFriend_IdUser2IsInvalid_ReturnsNotFoundStatus() throws Exception {
        User newUser1 = User.builder()
                .name("user")
                .login("login")
                .email("new@user.com")
                .birthday(LocalDate.of(1999, 1, 1)).build();
        userStorage.add(newUser1);

        User newUser2 = User.builder()
                .name("user2")
                .login("login2")
                .email("new2@user.com")
                .birthday(LocalDate.of(1999, 1, 1)).build();
        userStorage.add(newUser2);

        mockMvc.perform(put("/users/1/friends/999"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void handleDeleteFriend_IdsAreValid_ReturnsTrue() throws Exception {
        User newUser1 = User.builder()
                .name("user")
                .login("login")
                .email("new@user.com")
                .birthday(LocalDate.of(1999, 1, 1)).build();
        userStorage.add(newUser1);

        User newUser2 = User.builder()
                .name("user2")
                .login("login2")
                .email("new2@user.com")
                .birthday(LocalDate.of(1999, 1, 1)).build();
        userStorage.add(newUser2);
        mockMvc.perform(put("/users/1/friends/2"));

        mockMvc.perform(delete("/users/1/friends/2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.equalTo(true)));
    }

    @Test
    void handleDeleteFriend_IdUser1IsInvalid_ReturnsNotFoundStatus() throws Exception {
        User newUser1 = User.builder()
                .name("user")
                .login("login")
                .email("new@user.com")
                .birthday(LocalDate.of(1999, 1, 1)).build();
        userStorage.add(newUser1);

        User newUser2 = User.builder()
                .name("user2")
                .login("login2")
                .email("new2@user.com")
                .birthday(LocalDate.of(1999, 1, 1)).build();
        userStorage.add(newUser2);
        mockMvc.perform(put("/users/1/friends/2"));

        mockMvc.perform(delete("/users/999/friends/2"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void handleDeleteFriend_IdUser2IsInvalid_ReturnsNotFoundStatus() throws Exception {
        User newUser1 = User.builder()
                .name("user")
                .login("login")
                .email("new@user.com")
                .birthday(LocalDate.of(1999, 1, 1)).build();
        userStorage.add(newUser1);

        User newUser2 = User.builder()
                .name("user2")
                .login("login2")
                .email("new2@user.com")
                .birthday(LocalDate.of(1999, 1, 1)).build();
        userStorage.add(newUser2);
        mockMvc.perform(put("/users/1/friends/2"));

        mockMvc.perform(delete("/users/1/friends/999"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void handleGetCommonFriends_IdsAreValid_ReturnsValidResponseEntity() throws Exception {
        User newUser1 = User.builder()
                .name("user")
                .login("login")
                .email("new@user.com")
                .birthday(LocalDate.of(1999, 1, 1)).build();
        userStorage.add(newUser1);

        User newUser2 = User.builder()
                .name("user2")
                .login("login2")
                .email("new2@user.com")
                .birthday(LocalDate.of(1999, 1, 1)).build();
        userStorage.add(newUser2);

        User newUser3 = User.builder()
                .name("user3")
                .login("login3")
                .email("new3@user.com")
                .birthday(LocalDate.of(1999, 1, 1)).build();
        userStorage.add(newUser3);

        mockMvc.perform(put("/users/1/friends/3"));
        mockMvc.perform(put("/users/2/friends/3"));

        mockMvc.perform(get("/users/1/friends/common/2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", Matchers.equalTo(3)));
        mockMvc.perform(get("/users/2/friends/common/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", Matchers.equalTo(3)));
    }

    @Test
    void handleGetCommonFriends_IdUser1IsInvalid_ReturnsNotFoundStatus() throws Exception {
        User newUser1 = User.builder()
                .name("user")
                .login("login")
                .email("new@user.com")
                .birthday(LocalDate.of(1999, 1, 1)).build();
        userStorage.add(newUser1);

        User newUser2 = User.builder()
                .name("user2")
                .login("login2")
                .email("new2@user.com")
                .birthday(LocalDate.of(1999, 1, 1)).build();
        userStorage.add(newUser2);

        User newUser3 = User.builder()
                .name("user3")
                .login("login3")
                .email("new3@user.com")
                .birthday(LocalDate.of(1999, 1, 1)).build();
        userStorage.add(newUser3);

        mockMvc.perform(put("/users/1/friends/3"));
        mockMvc.perform(put("/users/2/friends/3"));

        mockMvc.perform(get("/users/999/friends/common/2"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void handleGetCommonFriends_IdUser2IsInvalid_ReturnsNotFoundStatus() throws Exception {
        User newUser1 = User.builder()
                .name("user")
                .login("login")
                .email("new@user.com")
                .birthday(LocalDate.of(1999, 1, 1)).build();
        userStorage.add(newUser1);

        User newUser2 = User.builder()
                .name("user2")
                .login("login2")
                .email("new2@user.com")
                .birthday(LocalDate.of(1999, 1, 1)).build();
        userStorage.add(newUser2);

        User newUser3 = User.builder()
                .name("user3")
                .login("login3")
                .email("new3@user.com")
                .birthday(LocalDate.of(1999, 1, 1)).build();
        userStorage.add(newUser3);

        mockMvc.perform(put("/users/1/friends/3"));
        mockMvc.perform(put("/users/2/friends/3"));

        mockMvc.perform(get("/users/1/friends/common/999"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}