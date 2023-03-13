package ru.yandex.practicum.filmorate.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.Month;

public class NotBeforeCinemaDayValidator implements ConstraintValidator<NotBeforeCinemaDay, LocalDate> {
    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value == null) return false;
        return !value.isBefore(LocalDate.of(1895, Month.DECEMBER, 28));
    }
}
