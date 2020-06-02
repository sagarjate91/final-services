package com.secure.search.customer.dto;
import com.secure.search.customer.model.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {

    private int id;
    private double grandTotal;
    private int cartLines;

    // linking the cart with a user
    private Customer Customer;
}
