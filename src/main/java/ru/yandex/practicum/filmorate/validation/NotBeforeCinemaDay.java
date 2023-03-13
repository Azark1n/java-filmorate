package ru.yandex.practicum.filmorate.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotBeforeCinemaDayValidator.class)
@Documented
public @interface NotBeforeCinemaDay {
    String message() default "May not before cinema day";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
