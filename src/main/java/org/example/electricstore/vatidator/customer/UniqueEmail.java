package org.example.electricstore.vatidator.customer;

import jakarta.validation.Constraint;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = UniqueEmailValidator.class)
@Target({java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.METHOD})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface UniqueEmail {
    String message() default "Email đã tồn tại !";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}
