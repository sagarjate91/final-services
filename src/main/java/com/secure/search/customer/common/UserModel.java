package com.secure.search.customer.common;

import com.secure.search.customer.model.Cart;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserModel implements Serializable {

	private int id;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private String mobileNumber;
	private String address;
	private String pinCode;

	private Cart cart;


}
