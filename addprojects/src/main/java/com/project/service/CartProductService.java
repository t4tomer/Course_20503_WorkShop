package com.project.service;

import com.project.model.CartProduct;
import com.project.model.Product;
import com.project.repository.CartProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CartProductService {
    public CartProduct addProductToCart(CartProduct newProductAdeed);

    public List<CartProduct> viewAll();

    public List<CartProduct> getAllProductsInCartOfUser(String clientEmail);

}
