package ru.yandex.practicum.filmorate.storage.film.impl;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Value
@RequiredArgsConstructor
@Component
@Qualifier("filmDbStorage")
public class FilmDbStorage implements FilmStorage {
    JdbcTemplate jdbcTemplate;

    @Override
    public void clearDB() {
    }

    @Override
    public Film add(Film film) {
        String sqlQuery = "INSERT INTO film (name, description, release_date, duration, mpa_id) " +
                "values (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        int newId = keyHolder.getKey().intValue();

        updateGenresByFilm(film, newId);
        updateLikesByFilm(film, newId);

        return getById(newId);
    }

    @Override
    public Film update(Film film) {
        String sqlQuery = "UPDATE film SET name = ?, description = ?, release_date = ?, duration = ?, " +
                "mpa_id = ? WHERE id = ?";
        jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), Date.valueOf(film.getReleaseDate()),
                film.getDuration(), film.getMpa().getId(), film.getId());

        updateGenresByFilm(film, film.getId());
        updateLikesByFilm(film, film.getId());

        return getById(film.getId());
    }

    @Override
    public List<Film> getAll() {
        String sql = "SELECT *, m.name AS mpa_name FROM film AS f LEFT JOIN mpa AS m ON f.mpa_id = m.id";
        return jdbcTemplate.query(sql, this::mapRow);
    }

    @Override
    public Film getById(int id) {
        String sql = "SELECT *, m.name AS mpa_name FROM film AS f LEFT JOIN mpa AS m ON f.mpa_id = m.id WHERE f.id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, this::mapRow, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(String.format("Film with id=%d not found", id));
        }
    }

    private Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("id");
         return Film.builder()
                .id(id)
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .mpa(Mpa.builder().id(rs.getInt("mpa_id")).name(rs.getString("mpa_name")).build())
                .genres(getGenresByFilm(id))
                .likes(getLikesByFilm(id))
                .build();
    }

    private Set<Genre> getGenresByFilm(int filmId) {
        String sql = "SELECT g.id, g.name FROM genre AS g JOIN film_genres as fg ON g.id = fg.genre_id WHERE fg.film_id = ?";
        return new HashSet<>(jdbcTemplate.query(sql, (rs, rowNum) ->
                Genre.builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("name"))
                        .build(), filmId));
    }

    private Set<Integer> getLikesByFilm(int filmId) {
        String sql = "SELECT fl.user_id FROM film_likes AS fl WHERE fl.film_id = ?";
        return new HashSet<>(jdbcTemplate.queryForList(sql, Integer.class, filmId));
    }

    private void updateGenresByFilm(Film film, int filmId) {
        jdbcTemplate.update("DELETE film_genres WHERE film_id = ?", film.getId());

        if (!film.getGenres().isEmpty()) {
            jdbcTemplate.batchUpdate("INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)",
                    film.getGenres(),
                    50,
                    (PreparedStatement ps, Genre genre) -> {
                        ps.setInt(1, filmId);
                        ps.setInt(2, genre.getId());
                    });
        }
    }

    private void updateLikesByFilm(Film film, int filmId) {
        jdbcTemplate.update("DELETE film_likes WHERE film_id = ?", film.getId());

        if (!film.getLikes().isEmpty()) {
            jdbcTemplate.batchUpdate("INSERT INTO film_likes (film_id, user_id) VALUES (?, ?)",
                    film.getLikes(),
                    50,
                    (PreparedStatement ps, Integer userId) -> {
                        ps.setInt(1, filmId);
                        ps.setInt(2, userId);
                    });
        }
    }
}
