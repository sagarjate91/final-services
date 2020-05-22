package com.secure.search.customer.controller;

import com.secure.search.customer.common.UserModel;
import com.secure.search.customer.model.Cart;
import com.secure.search.customer.repository.CardLineRepository;
import com.secure.search.customer.repository.CartRepository;
import com.secure.search.customer.repository.ProductRepository;
import com.secure.search.customer.service.CartLineServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@RequestMapping("/cart")
public class CheckOutController {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartLineServices cartLineServices;

    @Autowired
    CardLineRepository cardLineRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    private HttpSession session;

    private Cart getCart() {
        UserModel userModel=((UserModel)session.getAttribute("userModel"));
        Cart cart=new Cart();
        cart.setId(userModel.getId());
        return cart;
    }


    @GetMapping
    public String getChekOut(Model model){
        Cart cart = this.getCart();
        return "page";
    }
}
