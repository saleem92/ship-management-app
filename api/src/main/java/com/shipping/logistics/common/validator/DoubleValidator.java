package com.shipping.logistics.common.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DoubleValidator
		implements ConstraintValidator<ShipSize, Object> {

	private double min;

	@Override
	public void initialize(ShipSize constraintAnnotation) {
		this.min = constraintAnnotation.value();
	}

	public boolean isValid(Object value,
						   ConstraintValidatorContext context) {

		if (value instanceof Double) {
			return (double) value > min;
		}

		return false;
	}
}