package ru.yandex.practicum.filmorate.storage.user.impl;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@Value
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {
    UserDbStorage userDbStorage;
    private static User user1;
    private static User currentUser;

    @BeforeAll
    static void beforeAll() {
        user1 = User.builder()
                .name("user1")
                .login("login1")
                .email("new1@user.com")
                .birthday(LocalDate.of(1999, 1, 1))
                .build();
    }

    @Test
    void testAdd() {
        currentUser = userDbStorage.add(user1);

        assertThat(currentUser)
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("name", "user1");
    }

    @Test
    void testUpdate() {
        User updatedUser = currentUser.toBuilder()
                .name("updated user")
                .email("updated@email.com")
                .build();

        currentUser = userDbStorage.update(updatedUser);

        assertThat(currentUser)
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("name", "updated user")
                .hasFieldOrPropertyWithValue("email", "updated@email.com");
    }

    @Test
    void testGetAll() {
        List<User> allUsers = userDbStorage.getAll();

        AssertionsForInterfaceTypes.assertThat(allUsers).containsExactly(currentUser);
    }

    @Test
    void testGetById() {
        User userById = userDbStorage.getById(1);

        assertThat(userById).isEqualTo(currentUser);
    }

}