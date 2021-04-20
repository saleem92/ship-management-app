package com.shipping.logistics.api;

import com.shipping.logistics.AbstractIT;
import com.shipping.logistics.repository.entity.Ship;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DeleteShipIT extends AbstractIT {

	@Test
	@DisplayName("should throw error when Id not found")
	void shouldThrowErrorWhenIdNotFound() throws Exception {
		long shipId = 100;

		mvc.perform(delete(urlWithId, shipId)
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.errorCode", CoreMatchers.is(404)))
			.andExpect(jsonPath("$.message", CoreMatchers.is(String.format("Ship with id {%d} not found", shipId))));
	}

	@Test
	@DisplayName("should delete ship")
	void shouldDeleteShip() throws Exception {
		Ship ship1 = Ship.builder().name("Ship 1").code("AAAA-0000-A0")
			.length(1d).width(2d).build();
		Ship ship2 = Ship.builder().name("Ship 2").code("AAAA-0000-A1")
			.length(3d).width(4d).build();

		repository.saveAll(Arrays.asList(ship1, ship2));

		System.out.println(repository.findAll());

		mvc.perform(delete(urlWithId, 4)
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());

		List<Ship> ships = repository.findAll();

		assertThat(ships).hasSize(1);
	}
}
