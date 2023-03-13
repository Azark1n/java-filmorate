package ru.yandex.practicum.filmorate.validation;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@ToString
public class Violation {
    private final String fieldName;
    private final String message;

    public Violation(String fieldName, String message) {
        this.fieldName = fieldName;
        this.message = message;
        log.warn(this.toString());
    }
}
