package com.project.repository;

import com.project.model.CartProduct;
import com.project.model.CartProductId;
import com.project.model.Product;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartProductRepository extends CrudRepository<CartProduct, CartProductId> {
    // Additional query methods can be defined here if needed
    List<CartProduct> findByEmailCustomer(String clientEmail);

}
