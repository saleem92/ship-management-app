package com.shipping.logistics.api;

import com.shipping.logistics.model.ShipRequestDTO;
import com.shipping.logistics.model.ShipResponseDTO;
import com.shipping.logistics.repository.entity.Ship;
import com.shipping.logistics.service.ManageShipsService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static com.shipping.logistics.api.mapper.ShipMapper.INSTANCE;


/**
 * Controller to manage ship specific operations like GET/CREATE/UPDATE/DELETE
 * <p>
 * The context path for this controller is /ships
 * <p>
 * Validated annotation is used to validate the request object (as we've a custom annotation to validate size)
 */
@RestController
@RequestMapping("/ships")
@Validated
@RequiredArgsConstructor
public class ShipController {

	private final ManageShipsService service;

	@GetMapping
	@ApiOperation("Get all Ships")
	public ResponseEntity<List<ShipResponseDTO>> getShips() {
		return ResponseEntity.ok().body(INSTANCE.map(service.getShips()));
	}

	@GetMapping("/{id}")
	@ApiOperation("Get a ship by id")
	public ResponseEntity<ShipResponseDTO> getShip(@PathVariable long id) {
		return ResponseEntity.ok().body(INSTANCE.map(service.getShipById(id)));
	}

	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation("Create a new ship")
	public ResponseEntity<ShipResponseDTO> addShip(@RequestBody @Valid ShipRequestDTO shipRequestDTO, BindingResult bindingResult) throws URISyntaxException {
		Ship ship = service.createShip(INSTANCE.map(shipRequestDTO));
		return ResponseEntity.created(new URI("")).body(INSTANCE.map(ship));
	}

	@PatchMapping("/{id}")
	@ApiOperation("Update an existing ship")
	@ApiResponses({
		@ApiResponse(code = 200, message = "No Content", response = ShipResponseDTO.class),
		@ApiResponse(code = 404, message = "Not Found")
	})
	public ResponseEntity<ShipResponseDTO> patchShip(@PathVariable long id,
													 @RequestBody @Validated(ShipRequestDTO.OnPatch.class) ShipRequestDTO shipRequestDTO,
													 Errors errors) {
		if (errors.hasErrors()) {
			throw new IllegalArgumentException(errors.getAllErrors().get(0).getDefaultMessage());
		}

		Ship ship = service.patchShip(id, shipRequestDTO);

		return ResponseEntity.ok().body(INSTANCE.map(ship));
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation("Delete a ship")
	@ApiResponses({
		@ApiResponse(code = 204, message = "No Content"),
		@ApiResponse(code = 404, message = "Not Found")
	})
	public void deleteShip(@PathVariable long id) {
		service.deleteShip(id);
	}
}
