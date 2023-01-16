package ru.yandex.practicum.filmorate.annotations;

import org.hibernate.validator.internal.constraintvalidators.AbstractEmailValidator;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandles;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class CustomEmailValidator extends AbstractEmailValidator<ValidEmail> {
    private static final Log LOG = LoggerFactory.make(MethodHandles.lookup());
    private Pattern pattern;

    public CustomEmailValidator() {
    }

    public void initialize(ValidEmail emailAnnotation) {
        super.initialize(emailAnnotation);
        javax.validation.constraints.Pattern.Flag[] flags = emailAnnotation.flags();
        int intFlag = 0;
        javax.validation.constraints.Pattern.Flag[] var4 = flags;
        int var5 = flags.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            javax.validation.constraints.Pattern.Flag flag = var4[var6];
            intFlag |= flag.getValue();
        }

        if (!".*".equals(emailAnnotation.regexp()) || emailAnnotation.flags().length > 0) {
            try {
                this.pattern = Pattern.compile(emailAnnotation.regexp(), intFlag);
            } catch (PatternSyntaxException var8) {
                throw LOG.getInvalidRegularExpressionException(var8);
            }
        }

    }

    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        } else {
            boolean isValid = super.isValid(value, context);
            if (this.pattern != null && isValid) {
                Matcher m = this.pattern.matcher(value);
                return m.matches();
            } else {
                return isValid;
            }
        }
    }
}
