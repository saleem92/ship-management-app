package com.shipping.logistics.api;

import com.shipping.logistics.AbstractIT;
import com.shipping.logistics.model.ShipRequestDTO;
import com.shipping.logistics.repository.entity.Ship;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CreateShipIT extends AbstractIT {

	@Test
	@DisplayName("should throw error when input not valid")
	void shouldThrowErrorWhenInputIsNotValid() throws Exception {
		ShipRequestDTO dto = ShipRequestDTO.builder().name("Ship 1")
				.code("AAAA-0000-A1").build();

		MvcResult mvcResult = mvc.perform(post(url)
				.content(mapper.writeValueAsString(dto))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andReturn();

		String[] errorMessages = getErrorMessages(mvcResult);

		assertThat(errorMessages).containsExactlyInAnyOrder("width cannot not be null or should be greater than 0",
				"length cannot not be null or should be greater than 0");
	}

	@Test
	@DisplayName("should throw error when creating duplicate ship name")
	void shouldThrowErrorWhenTryToCreateDuplicateName() throws Exception {
		String name = "Ship 1";
		String code = "AAAA-0000-A1";
		double length = 1d;
		double width = 1.5;

		List<Ship> all = repository.findAll();

		ShipRequestDTO dto = ShipRequestDTO.builder().name(name)
				.code(code).length(length).width(width).build();

		mvc.perform(post(url)
				.content(mapper.writeValueAsString(dto))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());

		ShipRequestDTO dto1 = ShipRequestDTO.builder().name(name)
				.code(code).length(length).width(width).build();

		mvc.perform(post(url)
				.content(mapper.writeValueAsString(dto1))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.errorCode", is(409)))
				.andExpect(jsonPath("$.message", is("Ship name already exists")));
	}

	@Test
	@DisplayName("should create ship when input is valid")
	void shouldCreateShip() throws Exception {
		String name = "Ship 1000";
		String code = "AAAA-0000-A1";
		double length = 1d;
		double width = 1.5;

		ShipRequestDTO dto = ShipRequestDTO.builder().name(name)
				.code(code).length(length).width(width).build();

		mvc.perform(post(url)
				.content(mapper.writeValueAsString(dto))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());

		List<Ship> ships = repository.findAll();

		assertThat(ships).hasSize(1)
				.extracting(Ship::getId, Ship::getName, Ship::getCode,
						Ship::getLength, Ship::getWidth).contains(
				tuple(3L, name, code, length, width)
		);
	}
}
