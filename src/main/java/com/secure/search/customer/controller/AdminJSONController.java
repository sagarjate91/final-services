package com.secure.search.customer.controller;

import com.secure.search.customer.model.Customer;
import com.secure.search.customer.model.Product;
import com.secure.search.customer.service.ConstantService;
import com.secure.search.customer.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ConstantService.ADMIN)
public class AdminJSONController {

    @Autowired
    CustomerService service;


    @GetMapping("/all/Users")
    public List<Customer> users(){
        return service.users();
    }

    @PutMapping("/manage/{id}/activation")
    public String getUserUpdate(@PathVariable int id){
        return service.getUserUpdate(id);
    }

    @GetMapping("/json/data/all/products")
    public List<Product> products(){
        return service.products();
    }

    @GetMapping("/json/data/category/{categoryName}/products")
    public List<Product> productsCategory(@PathVariable("categoryName") String categoryName){
     return service.productsCategory(categoryName);

    }

    @PutMapping("/manage/product/{id}/activation")
    public String updateProduct(@PathVariable int id){
        return service.updateProduct(id);
    }


}
