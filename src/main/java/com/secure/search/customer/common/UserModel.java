package com.secure.search.customer.common;

import com.secure.search.customer.model.Cart;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserModel {

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
