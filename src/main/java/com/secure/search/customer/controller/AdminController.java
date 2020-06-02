package com.secure.search.customer.controller;

import com.secure.search.customer.dto.CategoryDTO;
import com.secure.search.customer.dto.CustomerDTO;
import com.secure.search.customer.dto.ProductDTO;
import com.secure.search.customer.exception.ProductNotFoundException;
import com.secure.search.customer.model.Category;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping(ConstantService.ADMIN)
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
        model.addAttribute(ConstantService.PROJECT_NAME, ConstantService.TITLE);
        model.addAttribute("userClickAdminHome",true);
        model.addAttribute(ConstantService.CATEGORIES, categoryRepository.findAll());
        model.addAttribute(ConstantService.TITLE, "Admin Home");
        if(message!=null){
           model.addAttribute(ConstantService.MESSAGE,message+"");
        }
        return "page";
    }
    
    @GetMapping({"/{id}/delete"})
    public String adminDelete(@PathVariable("")int id,RedirectAttributes redirectAttributes){
    	customerRepository.deleteById(id);
    	redirectAttributes.addFlashAttribute(ConstantService.MESSAGE, "User deleted successfully!");
        return "redirect:/admin/adminHome.htm";
    }
    
    @GetMapping({"/Product.htm"})
    public String adminProduct(Model model){
        model.addAttribute(ConstantService.PROJECT_NAME, ConstantService.TITLE);
        model.addAttribute("userClickAdminProduct",true);
        model.addAttribute(ConstantService.ACTION,"admin/product-upload");
        model.addAttribute(ConstantService.COMMAND,new ProductDTO());
        model.addAttribute(ConstantService.CATEGORIES, categoryRepository.findAll());
        model.addAttribute(ConstantService.CATEGORY_MODEL,new CategoryDTO());
        model.addAttribute(ConstantService.TITLE, "Admin Product");
        return "page";
    }

    @PostMapping("/manage/category")
    public String addCategory(@ModelAttribute("category") CategoryDTO category, Model model){

        Category category1=new Category();
        category1.setName(category.getName());
        category1.setDescription_category(category.getDescription_category());

        categoryRepository.save(category1);
        model.addAttribute(ConstantService.MESSAGE,"Category added successfully!");
        return "redirect:/admin/Product.htm";
    }

    @GetMapping({"/ProductList.htm"})
    public String adminProductList(Model model){
        model.addAttribute(ConstantService.PROJECT_NAME, ConstantService.TITLE);
        model.addAttribute("userClickAdminProductList",true);
        model.addAttribute(ConstantService.CATEGORIES, categoryRepository.findAll());
        model.addAttribute(ConstantService.TITLE, "Product List");
        return "page";
    }

    @GetMapping("/show/category/{categoryName}/products")
    public String adminCategoryProducts(@PathVariable("categoryName") String categoryName,Model model){
        model.addAttribute(ConstantService.PROJECT_NAME, ConstantService.TITLE);
        model.addAttribute("userClickCategoryProducts",true);
        model.addAttribute(ConstantService.CATEGORIES, categoryRepository.findAll());
        model.addAttribute(ConstantService.CATEGORY,categoryName);
        model.addAttribute(ConstantService.TITLE,categoryName);
        return "page";
    }

    @GetMapping("/show/{id}/product")
    public String showSingleProduct(@PathVariable int id,Model model) throws ProductNotFoundException {
        Product product=productRepository.findById(id).orElse(null);
        if(product==null){
            throw new ProductNotFoundException("Product not found exception");
        }
        model.addAttribute(ConstantService.PROJECT_NAME, ConstantService.TITLE);
        model.addAttribute("adminClickShowSingleProduct",true);
        model.addAttribute(ConstantService.CATEGORIES, categoryRepository.findAll());
        model.addAttribute(ConstantService.CATEGORY,product.getCategory());
        model.addAttribute(ConstantService.TITLE,product.getCategory());
        model.addAttribute(ConstantService.PRODUCT,product);
        return "page";
    }

    @GetMapping({"/manage/{id}/product"})
    public String adminProductUpdate(@PathVariable("id") int id, Model model){
        model.addAttribute(ConstantService.PROJECT_NAME, ConstantService.TITLE);
        model.addAttribute("userClickAdminProduct",true);
        model.addAttribute(ConstantService.ACTION,"admin/product-upload");
        model.addAttribute(ConstantService.COMMAND,productRepository.findById(id));
        model.addAttribute(ConstantService.CATEGORIES, categoryRepository.findAll());
        model.addAttribute(ConstantService.CATEGORY_MODEL,new CategoryDTO());
        return "page";
    }

    @PostMapping(value="/product-upload")
    public String productAdd(@Valid @ModelAttribute("command") ProductDTO product, HttpServletRequest request, BindingResult results, Model model){

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
            model.addAttribute(ConstantService.MESSAGE,"Validation fails for adding the product!");
            return "redirect:/admin/ProductList.htm";
        }

        if(!product.getFile().getOriginalFilename().equals("")){
            FileUploadUtility.uploadProductDetails(product.getFile(),product);
        }

        Product product1=new Product();
        product1.setProductId(product.getProductId());
        product1.setFileName(product.getFileName());
        product1.setPostName(product.getPostName());
        product1.setCategory(product.getCategory());
        product1.setModelNo(product.getModelNo());
        product1.setPrice(product.getPrice());
        product1.setQuantity(product.getQuantity());
        product1.setDescription(product.getDescription());
        productRepository.saveAndFlush(product1);

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

        model.addAttribute(ConstantService.MESSAGE, "product updated successfully!");
        return "redirect:/admin/ProductList.htm";
    }

    @PostMapping({"/admin-validate"})
    public String adminValidate(@ModelAttribute("command") CustomerDTO customer, Model model, HttpSession session, RedirectAttributes redirectAttributes){
        if(customer.getEmail().equalsIgnoreCase("admin@gmail.com") && customer.getPassword().equalsIgnoreCase("123")){
            addUserInSession(session,customer.getEmail(),ConstantService.ADMIN_ROLE);
            return "redirect:adminHome.htm";
        }else{
        	redirectAttributes.addFlashAttribute(ConstantService.MESSAGE, "Wrong Email and password,Please try new one");
            return "redirect:/customer/adminPanel.htm";
        }
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
        model.addAttribute("userClickHome",true);
        return "redirect:/customer/";
    }

}
