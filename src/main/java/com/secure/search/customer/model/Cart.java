package com.secure.search.customer.model;

import javax.persistence.*;
import java.io.Serializable;


@Entity
public class Cart implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
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

	public Cart(){

	}

	public Cart(int id, double grandTotal, int cartLines,Customer customer) {
		this.id = id;
		this.grandTotal = grandTotal;
		this.cartLines = cartLines;
		Customer = customer;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getGrandTotal() {
		return grandTotal;
	}

	public void setGrandTotal(double grandTotal) {
		this.grandTotal = grandTotal;
	}

	public int getCartLines() {
		return cartLines;
	}

	public void setCartLines(int cartLines) {
		this.cartLines = cartLines;
	}

	public Customer getCustomer() {
		return Customer;
	}

	public void setCustomer(Customer customer) {
		Customer = customer;
	}
}
