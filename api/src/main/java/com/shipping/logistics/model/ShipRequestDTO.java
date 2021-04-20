package com.shipping.logistics.model;

import com.shipping.logistics.common.validator.ShipSize;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.groups.Default;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ShipRequestDTO {

	@NotNull(message = "name cannot be null")
	private String name;

	@NotNull(message = "code cannot be null")
	@Pattern(message = "code format not valid", regexp = "^[a-zA-z]{4}-[0-9]{4}-[a-zA-z]{1}[0-9]{1}$",
			groups = {OnPatch.class, Default.class})
	private String code;

	@ShipSize(value = 0, message = "length cannot not be null or should be greater than 0")
	private Double length;

	@ShipSize(value = 0, message = "width cannot not be null or should be greater than 0")
	private Double width;

	public interface OnPatch {
	}
}
