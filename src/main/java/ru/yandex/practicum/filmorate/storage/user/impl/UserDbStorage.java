package ru.yandex.practicum.filmorate.storage.user.impl;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Value
@RequiredArgsConstructor
@Component
@Qualifier("userDbStorage")
public class UserDbStorage implements UserStorage {
    JdbcTemplate jdbcTemplate;

    @Override
    public void clearDB() {
    }

    @Override
    public List<User> getAll() {
        String sqlQuery = "SELECT * FROM \"user\"";
        return jdbcTemplate.query(sqlQuery, this::mapRow);
    }

    @Override
    public User add(User user) {
        String sqlQuery = "insert into \"user\" (name, login, email, birthday) " +
                "values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getEmail());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        int newId = keyHolder.getKey().intValue();

        updateUserFriends(user, newId);

        return getById(newId);
    }

    @Override
    public User update(User user) {
        String sqlQuery = "update \"user\" set name = ?, login = ?, email = ?, birthday = ? WHERE id = ?";
        jdbcTemplate.update(sqlQuery, user.getName(), user.getLogin(), user.getEmail(), Date.valueOf(user.getBirthday()), user.getId());

        updateUserFriends(user, user.getId());

        return getById(user.getId());
    }

    @Override
    public User getById(int id) {
        String sqlQuery = "SELECT * FROM \"user\" WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRow, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(String.format("User with id=%d not found", id));
        }
    }

    private User mapRow(ResultSet rs, int rowNum) throws SQLException {
        Date date = rs.getDate("birthday");
        LocalDate birthday = date == null ? null : date.toLocalDate();
        int id = rs.getInt("id");
        return User.builder()
                .id(id)
                .name(rs.getString("name"))
                .login(rs.getString("login"))
                .email(rs.getString("email"))
                .birthday(birthday)
                .friends(getFriendsByUser(id))
                .build();
    }

    private Set<Integer> getFriendsByUser(int userId) {
        String sql = "SELECT friend_id FROM user_friends WHERE user_id = ?";
        return new HashSet<>(jdbcTemplate.queryForList(sql, Integer.class, userId));
    }

    private void updateUserFriends(User user, int userId) {
        jdbcTemplate.update("DELETE FROM user_friends WHERE user_id = ?", user.getId());

        if (!user.getFriends().isEmpty()) {
            jdbcTemplate.batchUpdate("INSERT INTO user_friends (user_id, friend_id) VALUES (?, ?)",
                    user.getFriends(),
                    50,
                    (PreparedStatement ps, Integer friendId) -> {
                        ps.setInt(1, userId);
                        ps.setInt(2, friendId);
                    });
        }
    }
}
