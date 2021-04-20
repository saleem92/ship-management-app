package com.shipping.logistics.service;

import com.shipping.logistics.exception.NotFoundException;
import com.shipping.logistics.model.ShipRequestDTO;
import com.shipping.logistics.repository.ShipRepository;
import com.shipping.logistics.repository.entity.Ship;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ManageShipsServiceImplTest {

	@InjectMocks
	private ManageShipsServiceImpl service;

	@Mock
	private ShipRepository repository;

	@Test
	void shouldGetAllShips() {
		when(service.getShips()).thenReturn(Collections.singletonList(Ship.builder()
				.name("test").build()));

		List<Ship> ships = service.getShips();

		assertThat(ships).hasSize(1);
		assertThat(ships.get(0).getName()).isEqualTo("test");
	}

	@Test
	void shouldGetShipById() {
		Ship response = Ship.builder()
				.name("name").code("code").length(1.5).width(10).build();

		when(repository.findById(1L)).thenReturn(Optional.of(response));

		Ship ship = service.getShipById(1);

		assertSoftly(softAssertions -> {
			softAssertions.assertThat(ship.getName()).isEqualTo("name");
			softAssertions.assertThat(ship.getCode()).isEqualTo("code");
			softAssertions.assertThat(ship.getLength()).isEqualTo(1.5d);
			softAssertions.assertThat(ship.getWidth()).isEqualTo(10d);
		});
	}

	@Test
	void shouldThrowNotFoundWhenInvalidId() {
		doThrow(new NotFoundException("not found")).when(repository).findById(1L);

		assertThatThrownBy(() ->
				service.getShipById(1)).isInstanceOf(NotFoundException.class)
				.hasMessage("not found");
	}

	@Test
	void shouldPatchShip() {
		ShipRequestDTO dto = ShipRequestDTO.builder().name("name").build();
		Ship ship = Ship.builder().id(1L).name("test").code("code").length(1)
				.width(2)
				.build();

		Ship saved = Ship.builder().id(1L).name("name").code("code").length(1)
				.width(2)
				.build();

		when(repository.findById(1L)).thenReturn(Optional.of(ship));
		when(repository.save(ship)).thenReturn(saved);

		assertThat(service.patchShip(1, dto)).extracting(Ship::getName,
				Ship::getCode, Ship::getLength, Ship::getWidth)
				.contains("name", "code", 1.0, 2.0);
	}

	@Test
	void shouldThrowErrorWhenPatchIdNotFound() {
		when(repository.findById(1L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.patchShip(1, ShipRequestDTO.builder().build()))
				.isInstanceOf(NotFoundException.class).hasMessage("Ship with id {1} not found");

		verify(repository, never()).save(any());
	}
}