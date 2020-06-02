package com.secure.search.customer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Cart{


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name = "grand_total")
	private double grandTotal;
	@Column(name = "cart_lines")
	private int cartLines;

	// linking the cart with a user

	@OneToOne
	private Customer Customer;


}
