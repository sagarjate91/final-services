package com.secure.search.customer.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.secure.search.customer.model.Category;
import com.secure.search.customer.model.Customer;
import com.secure.search.customer.model.Product;
import com.secure.search.customer.model.ProductRecover;
import com.secure.search.customer.repository.CategoryRepository;
import com.secure.search.customer.repository.CustomerRepository;
import com.secure.search.customer.repository.ProductRecoverRepository;
import com.secure.search.customer.repository.ProductRepository;
import com.secure.search.customer.service.ConstantService;
import com.secure.search.customer.service.CustomerService;
import com.secure.search.customer.util.FileUploadUtility;
import com.secure.search.customer.validator.ProductValidator;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	Logger logger= LoggerFactory.getLogger(AdminController.class);

    @Autowired
    CustomerService service;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductRecoverRepository productRecoverRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @GetMapping({"/adminHome.htm"})
    public String adminHome(Model model,@ModelAttribute("message")String message){
        model.addAttribute("projectName", ConstantService.TITLE);
        model.addAttribute("userClickAdminHome",true);
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("title", "Admin Home");
        if(message!=null){
           model.addAttribute("message",message+"");
        }
        return "page";
    }
    
    @GetMapping({"/{id}/delete"})
    public String adminDelete(@PathVariable("")int id,RedirectAttributes redirectAttributes){
    	customerRepository.deleteById(id);
    	redirectAttributes.addFlashAttribute("message", "User deleted successfully!");
        return "redirect:/admin/adminHome.htm";
    }
    
    @GetMapping({"/Product.htm"})
    public String adminProduct(Model model){
        model.addAttribute("projectName", ConstantService.TITLE);
        model.addAttribute("userClickAdminProduct",true);
        model.addAttribute("action","admin/product-upload");
        model.addAttribute("command",new Product());
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("category-model",new Category());
        model.addAttribute("title", "Admin Product");
        return "page";
    }

    @PostMapping("/manage/category")
    public String addCategory(@ModelAttribute("category") Category category,Model model){
        categoryRepository.save(category);
        model.addAttribute("message","Category added successfully!");
        return "redirect:/admin/Product.htm";
    }


    @GetMapping({"/ProductList.htm"})
    public String adminProductList(Model model){
        model.addAttribute("projectName", ConstantService.TITLE);
        model.addAttribute("userClickAdminProductList",true);
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("title", "Product List");
        return "page";
    }

    @GetMapping("/show/category/{categoryName}/products")
    public String adminCategoryProducts(@PathVariable("categoryName") String categoryName,Model model){
        model.addAttribute("projectName", ConstantService.TITLE);
        model.addAttribute("userClickCategoryProducts",true);
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("category",categoryName);
        model.addAttribute("title",categoryName);
        return "page";
    }

    @GetMapping("/show/{id}/product")
    public String showSingleProduct(@PathVariable int id,Model model){
        Product product=productRepository.findById(id).orElse(null);
        model.addAttribute("projectName", ConstantService.TITLE);
        model.addAttribute("adminClickShowSingleProduct",true);
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("category",product.getCategory());
        model.addAttribute("title",product.getCategory());
        model.addAttribute("product",product);
        return "page";
    }

    @GetMapping({"/manage/{id}/product"})
    public String adminProductUpdate(@PathVariable("id") int id, Model model){
        model.addAttribute("projectName", ConstantService.TITLE);
        model.addAttribute("userClickAdminProduct",true);
        model.addAttribute("action","admin/product-upload");
        model.addAttribute("command",productRepository.findById(id));
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("category-model",new Category());
        return "page";
    }

    @PostMapping(value="/product-upload")
    public String productAdd(@Valid @ModelAttribute("command")Product product, HttpServletRequest request,BindingResult results,Model model){

        // mandatory file upload check
        if(product.getProductId() == 0) {
           new ProductValidator().validate(product, results);
         }
        else {
            // edit check only when the file has been selected
            if(!product.getFile().getOriginalFilename().equals("")) {
                new ProductValidator().validate(product, results);
            }
        }
        if(results.hasErrors()) {
            model.addAttribute("message", "Validation fails for adding the product!");
            return "redirect:/admin/ProductList.htm";
        }

        if(!product.getFile().getOriginalFilename().equals("")){
            FileUploadUtility.uploadProductDetails(product.getFile(),product);
        }

        product.setActive(0);
        product.setView(0);
        productRepository.saveAndFlush(product);

        // backup for product
        ProductRecover productRecover=new ProductRecover();
        productRecover.setProductId(product.getProductId());
        productRecover.setFileName(product.getFileName());
        productRecover.setPostName(product.getPostName());
        productRecover.setCategory(product.getCategory());
        productRecover.setModelNo(product.getModelNo());
        productRecover.setPrice(product.getPrice());
        productRecover.setQuantity(product.getQuantity());
        productRecover.setDescription(product.getDescription());
        productRecover.setActive(product.getActive());
        productRecover.setView(product.getView());
        productRecoverRepository.saveAndFlush(productRecover);

        model.addAttribute("message", "product updated successfully!");
        return "redirect:/admin/ProductList.htm";
    }

    @PostMapping({"/admin-validate"})
    public String adminValidate(@ModelAttribute("command") Customer customer, Model model, HttpSession session,RedirectAttributes redirectAttributes){
        if(customer.getEmail().equalsIgnoreCase("admin@gmail.com") && customer.getPassword().equalsIgnoreCase("123")){
            addUserInSession(session,customer.getEmail(),ConstantService.ADMIN_ROLE);
            return "redirect:adminHome.htm";
        }else{
        	redirectAttributes.addFlashAttribute("message", "Wrong Email and password,Please try new one");
            return "redirect:/customer/adminPanel.htm";
        }
    }

    public void addUserInSession(HttpSession session, String email, String role) {
        try{
            session.setAttribute("email", email);
            session.setAttribute("role", role);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session,Model model){
        session.invalidate();
        model.addAttribute("userClickHome",true);
        return "redirect:/customer/";
    }

}
