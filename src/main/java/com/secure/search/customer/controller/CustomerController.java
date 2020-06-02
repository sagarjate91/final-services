package com.secure.search.customer.controller;

import com.secure.search.customer.common.UserModel;
import com.secure.search.customer.dto.CustomerDTO;
import com.secure.search.customer.exception.ProductNotFoundException;
import com.secure.search.customer.model.Cart;
import com.secure.search.customer.model.Customer;
import com.secure.search.customer.model.Product;
import com.secure.search.customer.model.Role;
import com.secure.search.customer.repository.CartRepository;
import com.secure.search.customer.repository.CategoryRepository;
import com.secure.search.customer.repository.CustomerRepository;
import com.secure.search.customer.repository.ProductRepository;
import com.secure.search.customer.service.ConstantService;
import com.secure.search.customer.service.URLServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Collections;

@Controller
@RequestMapping(ConstantService.USER)
public class CustomerController {

    Logger logger= LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private CustomerRepository repo;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/customer/{userID}")
    public String getCustomer(@PathVariable("userID") int id,Model model){
        model.addAttribute(ConstantService.PROJECT_NAME, ConstantService.TITLE);
        model.addAttribute("userClickUserViewProfile",true);
        model.addAttribute(ConstantService.CATEGORIES, categoryRepository.findAll());
        Customer customer=repo.findById(id).orElse(null);
        model.addAttribute("customer",customer);
        return "page";
    }

    @PostMapping("/signup-add")
    public String user(@Valid @ModelAttribute("command") CustomerDTO customer1, RedirectAttributes redirectAttributes){
        if(repo.findByEmail(customer1.getEmail())!=null){
            redirectAttributes.addFlashAttribute("message","User Already added,Please try new one..!");
        }else{
        	
            Customer customer=new Customer();
            customer.setFirstName(customer1.getFirstName());
            customer.setLastName(customer1.getLastName());
            customer.setEmail(customer1.getEmail());
            customer.setPassword(customer1.getPassword());
            customer.setMobileNumber(customer1.getMobileNumber());
            customer.setAddress(customer1.getAddress());
            customer.setPinCode(customer1.getPinCode());
            customer.setPassword(bCryptPasswordEncoder.encode(customer.getPassword()));

            Role role=new Role();
            role.setRole("USER");
            customer.setRoles(Collections.singleton(role));
            Cart cart = new Cart();
            cart.setCustomer(customer);
            customer.setCart(cart);
            repo.save(customer);
            redirectAttributes.addFlashAttribute(ConstantService.MESSAGE,"User added successfully....!!!");
            
        }
         return "redirect:/customer/registerPanel.htm";
    }

    @PostMapping("/login-validate")
    public String loginValidate(@Valid @ModelAttribute("command")UserModel userModel,HttpSession session, RedirectAttributes redirectAttribute,Model model){

      Customer customer=repo.findByEmail(userModel.getEmail());
        if(customer==null){
            redirectAttribute.addAttribute(ConstantService.MESSAGE,"User does not exist");
            return URLServices.USER_URL;
        }
        if(!bCryptPasswordEncoder.matches(userModel.getPassword(),customer.getPassword())){
            redirectAttribute.addAttribute(ConstantService.MESSAGE,"Password mismatch");
            return URLServices.USER_URL;
        }
        if(customer.getStatus()!=1){
            redirectAttribute.addAttribute(ConstantService.MESSAGE,"Your Account not activated");
            return URLServices.USER_URL;
        }
        addUserInSession(session,customer.getEmail(),ConstantService.USER_ROLE);
        // set the name and the id
        userModel.setId(customer.getId());
        session.setAttribute("userModel", userModel);
        session.setAttribute("userID", customer.getId());
        model.addAttribute("userClickUserHome",true);
        return "redirect:/customer/user-home.htm";
    }

    @GetMapping("/show/{id}/product")
    public String showSingleProduct(@PathVariable int id, Model model) throws ProductNotFoundException {
        Product product=productRepository.findById(id).orElse(null);
        if(product==null){
            throw new ProductNotFoundException("Product not found");
        }
        model.addAttribute(ConstantService.PROJECT_NAME, ConstantService.TITLE);
        model.addAttribute("userClickShowSingleProduct",true);
        model.addAttribute(ConstantService.CATEGORY,product.getCategory());
        model.addAttribute(ConstantService.PRODUCT,product);
        model.addAttribute(ConstantService.CATEGORIES, categoryRepository.findAll());
        return "page";
    }

    @GetMapping("/show/category/{categoryName}/products")
    public String adminCategoryProducts(@PathVariable("categoryName") String categoryName,Model model){
        model.addAttribute(ConstantService.PROJECT_NAME, ConstantService.TITLE);
        model.addAttribute("userClickHomeCategory",true);
        model.addAttribute(ConstantService.CATEGORIES, categoryRepository.findAll());
        model.addAttribute(ConstantService.CATEGORY,categoryName);
        model.addAttribute(ConstantService.TITLE,categoryName);
        return "page";
    }

    @GetMapping(value = {"/userHome.htm","user-home.htm"})
    public String userHome(Model model){
        model.addAttribute("userClickUserHome",true);
        model.addAttribute(ConstantService.TITLE,"All Products");
        model.addAttribute(ConstantService.CATEGORIES, categoryRepository.findAll());
        return "page";
    }

    @GetMapping("/{userID}/view-profile.htm")
    public String viewProfile(@PathVariable("userID")int id, Model model){
        model.addAttribute("userClickUserViewProfile",true);
        model.addAttribute(ConstantService.TITLE,"View Profile");
        model.addAttribute(ConstantService.CATEGORIES, categoryRepository.findAll());
        Customer customer=repo.findById(id).orElse(null);
        model.addAttribute("customer",customer);
        return "page";
    }

    @GetMapping("/purchase-product.htm")
    public String purchaseProduct(Model model,@ModelAttribute("title") String title){
        model.addAttribute("userClickUserPurchaseProduct",true);
        model.addAttribute(ConstantService.TITLE,"Purchase Product");
        model.addAttribute(ConstantService.CATEGORIES, categoryRepository.findAll());
        return "page";
    }

    @GetMapping("/my-order.htm")
    public String myOrderProduct(Model model,@ModelAttribute("title") String title){
        model.addAttribute("userClickUserMyOrder",true);
        model.addAttribute(ConstantService.TITLE,"My Order");
        model.addAttribute(ConstantService.CATEGORIES, categoryRepository.findAll());
        return "page";
    }

    public void addUserInSession(HttpSession session, String email, String role) {
        try{
            session.setAttribute("email", email);
            session.setAttribute("role", role);
        }catch(Exception e){
           logger.error(e.getMessage());
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session,Model model){
        session.invalidate();
        model.addAttribute(ConstantService.MESSAGE,"Logout Successfully done...!!");
        return "redirect:/customer/";
    }
}
