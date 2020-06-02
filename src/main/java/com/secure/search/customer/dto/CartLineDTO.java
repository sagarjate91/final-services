package com.secure.search.customer.dto;

import com.secure.search.customer.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartLineDTO {

    private int id;
    private int cartId;
    private int productCount;
    private double total;
    private double buyingPrice;
    private boolean available = true;

    private Product product;

}
