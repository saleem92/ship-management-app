package com.shipping.logistics.common;

import lombok.Data;

import java.util.function.Consumer;

/**
 * Patch data if it is not null
 *
 * @param <T> type of object
 */
@Data
public final class PatchValue<T> {
	private final T value;

	private PatchValue(T value) {
		this.value = value;
	}

	/**
	 * Static method to wrap a object with PatchValue
	 *
	 * @param value the actual value
	 * @param <T>   type of object
	 * @return wrapper over actual value
	 */
	public static <T> PatchValue<T> of(T value) {
		return new PatchValue<>(value);
	}

	/**
	 * Method to set values to object only when the patchValue is not null
	 *
	 * @param setter     the object where the value need to be set
	 * @param patchValue the value to be patched
	 * @param <T>        type of object
	 */
	public static <T> void patchIfPresent(Consumer<T> setter, PatchValue<T> patchValue) {
		if (patchValue.getValue() != null) {
			setter.accept(patchValue.getValue());
		}
	}

	public T getValue() {
		return value;
	}
}
