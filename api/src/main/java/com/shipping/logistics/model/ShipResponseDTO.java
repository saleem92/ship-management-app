package com.shipping.logistics.model;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ShipResponseDTO {

	private long id;
	private String name;
	private String code;
	private Double length;
	private Double width;
}
