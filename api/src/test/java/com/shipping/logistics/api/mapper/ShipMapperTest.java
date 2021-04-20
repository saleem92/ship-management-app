package com.shipping.logistics.api.mapper;

import com.shipping.logistics.model.ShipRequestDTO;
import com.shipping.logistics.model.ShipResponseDTO;
import com.shipping.logistics.repository.entity.Ship;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ShipMapperTest {

	private static final String code = "code 1";
	private static final String name = "ship 1";
	private static final double length = 0.1;
	private static final double width = 0.2;

	private final ShipMapper mapper = ShipMapper.INSTANCE;

	@Test
	void shouldMapDTOToEntity() {
		ShipRequestDTO dto = ShipRequestDTO.builder()
				.code(code).name(name)
				.length(length).width(width).build();

		assertThat(mapper.map(dto)).extracting(Ship::getName, Ship::getCode,
				Ship::getLength, Ship::getWidth).contains(name, code, length, width);
	}

	@Test
	void shouldMapEntityToResponseDTO() {
		Ship shp1 = Ship.builder()
				.code(code).name(name)
				.length(length).width(width).build();
		Ship shp2 = Ship.builder()
				.code("code 2").name("name 2")
				.length(length).width(width).build();

		List<Ship> ships = Arrays.asList(shp1, shp2);

		assertThat(mapper.map(ships)).extracting(ShipResponseDTO::getName, ShipResponseDTO::getCode,
				ShipResponseDTO::getLength, ShipResponseDTO::getWidth)
				.containsExactlyInAnyOrder(Tuple.tuple(name, code, length, width),
						Tuple.tuple("name 2", "code 2", length, width));
	}
}