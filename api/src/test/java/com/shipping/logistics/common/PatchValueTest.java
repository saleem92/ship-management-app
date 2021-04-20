package com.shipping.logistics.common;

import com.shipping.logistics.model.ShipRequestDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class PatchValueTest {

	@Test
	void shouldSetValueWhenInputValueIsNotEmpty() {
		ShipRequestDTO dto = new ShipRequestDTO();
		PatchValue.patchIfPresent(dto::setCode, PatchValue.of("test"));

		Assertions.assertThat(dto.getCode()).isEqualTo("test");
	}

	@Test
	void shouldNotSetValueWhenInputIsNull() {
		ShipRequestDTO dto = new ShipRequestDTO();
		PatchValue.patchIfPresent(dto::setCode, PatchValue.of(null));

		Assertions.assertThat(dto.getCode()).isNull();
	}

}