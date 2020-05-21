package com.secure.search.customer.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "order_detail")
public class OrderDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@ManyToOne
	@JoinColumn(name = "user_id")
	private Customer customer;
	@Column(name = "order_total")
	private double orderTotal;

	@Column(name = "order_count")
	private int orderCount;
	
	@Column(name="order_date")
	private Date orderDate;

	public OrderDetail(){

	}

	public OrderDetail(int id, Customer customer, double orderTotal, int orderCount, Date orderDate) {
		this.id = id;
		this.customer = customer;
		this.orderTotal = orderTotal;
		this.orderCount = orderCount;
		this.orderDate = orderDate;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public double getOrderTotal() {
		return orderTotal;
	}

	public void setOrderTotal(double orderTotal) {
		this.orderTotal = orderTotal;
	}

	public int getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(int orderCount) {
		this.orderCount = orderCount;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
}
