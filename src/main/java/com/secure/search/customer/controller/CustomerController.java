package com.secure.search.customer.controller;

import com.secure.search.customer.common.UserModel;
import com.secure.search.customer.model.Customer;
import com.secure.search.customer.model.Product;
import com.secure.search.customer.model.Role;
import com.secure.search.customer.repository.CategoryRepository;
import com.secure.search.customer.repository.CustomerRepository;
import com.secure.search.customer.repository.ProductRepository;
import com.secure.search.customer.service.ConstantService;
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
@RequestMapping("/customer")
public class CustomerController {

    Logger logger= LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private CustomerRepository repo;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/signup-add")
    public String user(@Valid @ModelAttribute("command") Customer customer,RedirectAttributes redirectAttributes){
        if(repo.findByEmail(customer.getEmail())!=null){
            redirectAttributes.addFlashAttribute("message","User Already added,Please try new one..!");
        }else{
            customer.setPassword(bCryptPasswordEncoder.encode(customer.getPassword()));
            customer.setStatus(0);
            Role role=new Role();
            role.setRole("USER");
            customer.setRoles(Collections.singleton(role));
            repo.save(customer);
            redirectAttributes.addFlashAttribute("message","User added successfully....!!!");
        }
         return "redirect:/customer/registerPanel.htm";
    }


    @PostMapping("/login-validate")
    public String loginValidate(@Valid @ModelAttribute("command")UserModel userModel,HttpSession session, RedirectAttributes redirectAttribute,Model model){

      Customer customer=repo.findByEmail(userModel.getEmail());
        if(customer==null){
            redirectAttribute.addAttribute("message","User does not exist");
            return "redirect:/customer/user.htm";
        }
        if(!bCryptPasswordEncoder.matches(userModel.getPassword(),customer.getPassword())){
            redirectAttribute.addAttribute("message","Password mismatch");
            return "redirect:/customer/user.htm";
        }
        if(customer.getStatus()!=1){
            redirectAttribute.addAttribute("message","Your Account not activated");
            return "redirect:/customer/user.htm";
        }
        addUserInSession(session,customer.getEmail(),ConstantService.USER_ROLE);
        // set the name and the id
        userModel.setId(customer.getId());
        session.setAttribute("userModel", userModel);
        model.addAttribute("userClickUserHome",true);
        return "redirect:/customer/user-home.htm";
    }

    @GetMapping("/show/{id}/product")
    public String showSingleProduct(@PathVariable int id, Model model){
        Product product=productRepository.findById(id).orElse(null);
        model.addAttribute("projectName", ConstantService.TITLE);
        model.addAttribute("userClickShowSingleProduct",true);
        model.addAttribute("category",product.getCategory());
        model.addAttribute("product",product);
        model.addAttribute("categories", categoryRepository.findAll());
        return "page";
    }

    @GetMapping("/show/category/{categoryName}/products")
    public String adminCategoryProducts(@PathVariable("categoryName") String categoryName,Model model){
        model.addAttribute("projectName", ConstantService.TITLE);
        model.addAttribute("userClickHomeCategory",true);
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("category",categoryName);
        model.addAttribute("title",categoryName);
        return "page";
    }

    @GetMapping(value = {"/userHome.htm","user-home.htm"})
    public String userHome(Model model){
        model.addAttribute("userClickUserHome",true);
        model.addAttribute("title","All Products");
        model.addAttribute("categories", categoryRepository.findAll());
        return "page";
    }

    @GetMapping("/view-profile.htm")
    public String viewProfile(Model model){
        model.addAttribute("userClickUserViewProfile",true);
        model.addAttribute("title","View Profile");
        model.addAttribute("categories", categoryRepository.findAll());
        return "page";
    }

    @GetMapping("/purchase-product.htm")
    public String purchaseProduct(Model model,@ModelAttribute("title") String title){
        model.addAttribute("userClickUserPurchaseProduct",true);
        model.addAttribute("title","Purchase Product");
        model.addAttribute("categories", categoryRepository.findAll());
        return "page";
    }

    @GetMapping("/my-order.htm")
    public String myOrderProduct(Model model,@ModelAttribute("title") String title){
        model.addAttribute("userClickUserMyOrder",true);
        model.addAttribute("title","My Order");
        model.addAttribute("categories", categoryRepository.findAll());
        return "page";
    }

    public void addUserInSession(HttpSession session, String email, String role) {
        try{
            session.setAttribute("email", email);
            session.setAttribute("role", role);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session,Model model){
        session.invalidate();
        model.addAttribute("message","Logout Successfully done...!!");
        return "redirect:/customer/";
    }
}
