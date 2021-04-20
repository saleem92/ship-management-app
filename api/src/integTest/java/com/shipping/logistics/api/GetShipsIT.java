package com.shipping.logistics.api;

import com.shipping.logistics.AbstractIT;
import com.shipping.logistics.model.ShipResponseDTO;
import com.shipping.logistics.repository.entity.Ship;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GetShipsIT extends AbstractIT {

	@Test
	@DisplayName("should get all ships")
	void shouldGetAllShips() throws Exception {
		Ship ship1 = Ship.builder().name("Ship 1").code("code 1")
				.length(1d).width(2d).build();
		Ship ship2 = Ship.builder().name("Ship 2").code("code 2")
				.length(3d).width(4d).build();

		repository.saveAll(Arrays.asList(ship1, ship2));

		MvcResult mvcResult = mvc.perform(get(url)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();

		List<ShipResponseDTO> response = mapper.readValue(mvcResult.getResponse().getContentAsString(),
				mapper.getTypeFactory().constructCollectionType(List.class, ShipResponseDTO.class));

		assertThat(response).hasSize(2).extracting(ShipResponseDTO::getId,
				ShipResponseDTO::getName, ShipResponseDTO::getCode,
				ShipResponseDTO::getLength, ShipResponseDTO::getWidth)
				.contains(
						tuple(ship1.getId(),
								ship1.getName(), ship1.getCode(), ship1.getLength(), ship1.getWidth()),

						tuple(ship2.getId(), ship2.getName(),
								ship2.getCode(), ship2.getLength(), ship2.getWidth()));
	}
}
