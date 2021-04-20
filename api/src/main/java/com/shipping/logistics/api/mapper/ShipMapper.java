package com.shipping.logistics.api.mapper;

import com.shipping.logistics.model.ShipRequestDTO;
import com.shipping.logistics.model.ShipResponseDTO;
import com.shipping.logistics.repository.entity.Ship;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ShipMapper {

    ShipMapper INSTANCE = Mappers.getMapper(ShipMapper.class);

    List<ShipResponseDTO> map(List<Ship> ship);

    ShipResponseDTO map(Ship ship);

    Ship map(ShipRequestDTO ship);
}
