package com.secure.search.customer.repository;

import com.secure.search.customer.model.CartLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CardLineRepository extends JpaRepository<CartLine,Integer> {

    @Query(value ="FROM CartLine WHERE cartId = ?1 AND product.productId =?2",nativeQuery = false)
    CartLine getByCartAndProduct(int id, int productId);

    @Query(value ="FROM CartLine WHERE cartId =?1",nativeQuery = false)
    List<CartLine> list(int id);

}
