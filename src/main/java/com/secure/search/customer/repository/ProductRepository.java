package com.secure.search.customer.repository;

import com.secure.search.customer.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Integer> {
    List<Product> findByCategory(String categoryName);
}
