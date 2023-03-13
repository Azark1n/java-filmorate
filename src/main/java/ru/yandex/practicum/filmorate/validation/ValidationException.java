package ru.yandex.practicum.filmorate.validation;

import lombok.Getter;

public class ValidationException extends RuntimeException{
    @Getter
    private final String fieldName;
    public ValidationException(String fieldName, String message) {
        super(message);
        this.fieldName = fieldName;
    }
}
