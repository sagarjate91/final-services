package com.secure.search.customer.controller;

import com.secure.search.customer.common.UserModel;
import com.secure.search.customer.model.Customer;
import com.secure.search.customer.repository.CardLineRepository;
import com.secure.search.customer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpSession;

@ControllerAdvice
public class GlobalController {

	@Autowired
	private CustomerRepository userDAO;
	
	@Autowired
	private HttpSession session;
	
	public UserModel userModel = null;
	public Customer user = null;

	@Autowired
	CardLineRepository cardLineRepository;
	
	@ModelAttribute("userModel")
	public UserModel getUserModel() {		

	   if(session.getAttribute("email")!=null) {

				   // get the user from the database
				   user = userDAO.findByEmail(session.getAttribute("email").toString());

				   if (user != null) {
					   // create a new model
					   userModel = new UserModel();
					   // set the name and the id
					   userModel.setId(user.getId());
					   userModel.setFirstName(user.getFirstName() + " " + user.getLastName());
					   userModel.setCart(user.getCart());
					   session.setAttribute("userModel", userModel);
					   return userModel;
				   }
		}
		return (UserModel) session.getAttribute("userModel");
	}
		
}
