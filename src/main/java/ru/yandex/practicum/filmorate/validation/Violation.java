package ru.yandex.practicum.filmorate.validation;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Violation {
    private final String fieldName;
    private final String message;

    public Violation(String fieldName, String message) {
        this.fieldName = fieldName;
        this.message = message;
    }
}
