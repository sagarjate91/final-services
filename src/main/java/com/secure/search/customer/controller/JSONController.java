package com.secure.search.customer.controller;

import com.secure.search.customer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/customer")
public class JSONController {
	
	@Autowired
	private CustomerRepository repo;
	


}
