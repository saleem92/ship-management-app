package com.shipping.logistics.service;

import com.shipping.logistics.common.PatchValue;
import com.shipping.logistics.exception.NotFoundException;
import com.shipping.logistics.model.ShipRequestDTO;
import com.shipping.logistics.repository.ShipRepository;
import com.shipping.logistics.repository.entity.Ship;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service layer to manage ship details
 */
@Service
@RequiredArgsConstructor
public class ManageShipsServiceImpl implements ManageShipsService {

	private final ShipRepository repository;

	public List<Ship> getShips() {
		return repository.findAll();
	}

	public Ship getShipById(long id) {
		return getShip(id);
	}

	private Ship getShip(long id) {
		return repository.findById(id).orElseThrow(() ->
				new NotFoundException(String.format("Ship with id {%d} not found", id)));
	}

	public Ship createShip(Ship ship) {
		return repository.save(ship);
	}

	public void deleteShip(long id) {
		repository.delete(getShip(id));
	}

	public Ship patchShip(long id, ShipRequestDTO shipRequestDTO) {
		Ship ship = getShip(id);

		PatchValue.patchIfPresent(ship::setName, PatchValue.of(shipRequestDTO.getName()));
		PatchValue.patchIfPresent(ship::setCode, PatchValue.of(shipRequestDTO.getCode()));
		PatchValue.patchIfPresent(ship::setWidth, PatchValue.of(shipRequestDTO.getWidth()));
		PatchValue.patchIfPresent(ship::setLength, PatchValue.of(shipRequestDTO.getLength()));

		return repository.save(ship);
	}
}
