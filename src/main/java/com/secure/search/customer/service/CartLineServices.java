package com.secure.search.customer.service;

import com.secure.search.customer.common.UserModel;
import com.secure.search.customer.model.Cart;
import com.secure.search.customer.model.CartLine;
import com.secure.search.customer.model.Product;
import com.secure.search.customer.repository.CardLineRepository;
import com.secure.search.customer.repository.CartRepository;
import com.secure.search.customer.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class CartLineServices {

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

    public List<CartLine> getCartLines() {

        return cardLineRepository.findAll();

    }

    public String addCartLine(int productId) {
        Cart cart = this.getCart();
        String response = null;
        CartLine cartLine = cardLineRepository.getByCartAndProduct(cart.getId(), productId);
        if(cartLine==null) {
            // add a new cartLine if a new product is getting added
            cartLine = new CartLine();
            Product product = productRepository.findById(productId).orElse(null);
            // transfer the product details to cartLine
            cartLine.setCartId(cart.getId());
            cartLine.setProduct(product);
            cartLine.setProductCount(1);
            cartLine.setBuyingPrice(product.getPrice());
            cartLine.setTotal(product.getPrice());

            // insert a new cartLine
            cardLineRepository.save(cartLine);

            // update the cart
            cart.setGrandTotal(cart.getGrandTotal() + cartLine.getTotal());
            cart.setCartLines(cart.getCartLines() + 1);
            cartRepository.saveAndFlush(cart);

            response = "result=added";
        }
        else {
            // check if the cartLine has been already reached to maximum count
            if(cartLine.getProductCount() < 3) {
                // call the manageCartLine method to increase the count
                response = this.manageCartLine(cartLine.getId(), cartLine.getProductCount() + 1);
            }
            else {
                response = "result=maximum";
            }
        }
        return response;
    }

    /* to update the cart count */
    public String manageCartLine(int cartLineId, int count) {

        CartLine cartLine = cardLineRepository.findById(cartLineId).orElse(null);

        double oldTotal = cartLine.getTotal();

        Product product = cartLine.getProduct();

        // check if that much quantity is available or not
        if(product.getQuantity() < count) {
            return "result=unavailable";
        }

        // update the cart line
        cartLine.setProductCount(count);
        cartLine.setBuyingPrice(product.getPrice());
        cartLine.setTotal(product.getPrice() * count);
        cardLineRepository.saveAndFlush(cartLine);

        // update the cart
        Cart cart = this.getCart();
        cart.setGrandTotal(cart.getGrandTotal() - oldTotal + cartLine.getTotal());
        cartRepository.saveAndFlush(cart);

        return "result=updated";
    }

    public String validateCartLine() {


        Cart cart = this.getCart();
        List<CartLine> cartLines = cardLineRepository.list(cart.getId());
        double grandTotal = 0.0;
        int lineCount = 0;
        String response = "result=success";
        boolean changed = false;
        Product product = null;
        for(CartLine cartLine : cartLines) {
            product = cartLine.getProduct();
            changed = false;
            // check if the product is active or not
            // if it is not active make the availability of cartLine as false
            if((product.getActive()==0 && product.getQuantity() == 0) && cartLine.isAvailable()) {
                cartLine.setAvailable(false);
                changed = true;
            }
            // check if the cartLine is not available
            // check whether the product is active and has at least one quantity available
            if((product.getActive()!=0 && product.getQuantity() > 0) && !(cartLine.isAvailable())) {
                cartLine.setAvailable(true);
                changed = true;
            }

            // check if the buying price of product has been changed
            if(cartLine.getBuyingPrice() != product.getPrice()) {
                // set the buying price to the new price
                cartLine.setBuyingPrice(product.getPrice());
                // calculate and set the new total
                cartLine.setTotal(cartLine.getProductCount() * product.getPrice());
                changed = true;
            }

            // check if that much quantity of product is available or not
            if(cartLine.getProductCount() > product.getQuantity()) {
                cartLine.setProductCount(product.getQuantity());
                cartLine.setTotal(cartLine.getProductCount() * product.getPrice());
                changed = true;

            }

            // changes has happened
            if(changed) {
                //update the cartLine
                cardLineRepository.saveAndFlush(cartLine);
                // set the result as modified
                response = "result=modified";
            }

            grandTotal += cartLine.getTotal();
            lineCount++;
        }

        cart.setCartLines(lineCount++);
        cart.setGrandTotal(grandTotal);
        cartRepository.saveAndFlush(cart);

        return response;
    }

    public String removeCartLine(int cartLineId) {

       CartLine cartLine = cardLineRepository.findById(cartLineId).orElse(null);
        // deduct the cart
        // update the cart
        Cart cart = this.getCart();
        cart.setGrandTotal(cart.getGrandTotal() - cartLine.getTotal());
        cart.setCartLines(cart.getCartLines() - 1);
        cartRepository.saveAndFlush(cart);

        // remove the cartLine
        cardLineRepository.delete(cartLine);
        return "result=deleted";
    }
}
