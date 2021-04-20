package com.shipping.logistics.service;

import com.shipping.logistics.model.ShipRequestDTO;
import com.shipping.logistics.repository.entity.Ship;

import java.util.List;

public interface ManageShipsService {

    List<Ship> getShips();

    Ship getShipById(long id);

    Ship createShip(Ship ship);

    void deleteShip(long id);

    Ship patchShip(long id, ShipRequestDTO shipRequestDTO);
}
