package ru.yandex.practicum.filmorate.annotations;

import javax.validation.Constraint;
import javax.validation.OverridesAttribute;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Pattern;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(
        validatedBy = {CustomEmailValidator.class}
)
@ReportAsSingleViolation

public @interface ValidEmail {
    String message() default "Not valid email or empty!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @OverridesAttribute(
            constraint = Pattern.class,
            name = "regexp"
    )
    String regexp() default ".*";

    @OverridesAttribute(
            constraint = Pattern.class,
            name = "flags"
    )
    Pattern.Flag[] flags() default {};

    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface List {
        ValidEmail[] value();
    }
}
