package com.shipping.logistics.repository.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "ship_details")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
@Builder
public class Ship {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@Column(unique = true, length = 15)
	private String name;

	@Column(nullable = false, length = 14)
	private String code;

	@Column(nullable = false)
	private double length;

	@Column(nullable = false)
	private double width;
}
