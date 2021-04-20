package com.shipping.logistics.common;

import com.shipping.logistics.common.validator.DoubleValidator;
import com.shipping.logistics.common.validator.ShipSize;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Annotation;

class DoubleValidatorTest {

	@MockBean
	private ConstraintValidatorContext context;
	private DoubleValidator doubleValidator;

	@BeforeEach
	void setUp() {
		doubleValidator = new DoubleValidator();
	}

	@Test
	void shouldReturnTrueWhenInputIsValid() {
		doubleValidator.initialize(new SizeValidator());
		Assertions.assertTrue(doubleValidator.isValid(1d, context));
	}

	@Test
	void shouldReturnFalseWhenInputIsNotValid() {
		doubleValidator.initialize(new SizeValidator());
		Assertions.assertFalse(doubleValidator.isValid(0d, context));
	}

	@Test
	void shouldReturnFalseWhenInputIsNotDoubleValue() {
		doubleValidator.initialize(new SizeValidator());
		Assertions.assertFalse(doubleValidator.isValid(1, context));
	}
}

class SizeValidator implements ShipSize {

	@Override
	public String message() {
		return "should be greater than 0";
	}

	@Override
	public Class<?>[] groups() {
		return new Class[0];
	}

	@Override
	public Class<? extends Payload>[] payload() {
		return new Class[0];
	}

	@Override
	public double value() {
		return 0;
	}

	@Override
	public Class<? extends Annotation> annotationType() {
		return ShipSize.class;
	}
}