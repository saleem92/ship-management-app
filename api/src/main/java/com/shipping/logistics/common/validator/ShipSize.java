package com.shipping.logistics.common.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = DoubleValidator.class
)
public @interface ShipSize {
    String message() default "{javax.validation.constraints.ShipSize.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    double value() default 0.1;
}
