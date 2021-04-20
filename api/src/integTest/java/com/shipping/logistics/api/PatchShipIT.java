package com.shipping.logistics.api;

import com.shipping.logistics.AbstractIT;
import com.shipping.logistics.model.ShipRequestDTO;
import com.shipping.logistics.repository.entity.Ship;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PatchShipIT extends AbstractIT {

	@Test
	@DisplayName("should throw error when Id not found")
	void shouldThrowErrorWhenIdNotFound() throws Exception {
		ShipRequestDTO dto = ShipRequestDTO.builder()
				.code("AAAA-0000-A1").name("name").width(1d).length(1d).build();

		mvc.perform(patch(urlWithId, 100)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(dto)))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.errorCode", CoreMatchers.is(404)))
				.andExpect(jsonPath("$.message", CoreMatchers.is("Ship with id {100} not found")));
	}

	@Test
	@DisplayName("should throw error when input is not valid")
	void shouldPatchData() throws Exception {
		Ship ship = Ship.builder().name("name").code("xxxx-0000-x0").width(1d).length(2d).build();
		repository.save(ship);

		ShipRequestDTO dto = ShipRequestDTO.builder().code("AAAA-0000-A1").name("Ship 100").build();

		List<Ship> all = repository.findAll();

		System.out.println(all);
		mvc.perform(patch(urlWithId, 8)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(dto)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", CoreMatchers.is(8)))
				.andExpect(jsonPath("$.name", CoreMatchers.is("Ship 100")))
				.andExpect(jsonPath("$.code", CoreMatchers.is("AAAA-0000-A1")))
				.andExpect(jsonPath("$.length", CoreMatchers.is(2d)))
				.andExpect(jsonPath("$.width", CoreMatchers.is(1d)));
	}

}
