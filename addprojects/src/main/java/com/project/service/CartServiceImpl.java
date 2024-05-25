package com.project.service;

import com.project.model.Product;
import com.project.repository.CartRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.project.model.Image;
import com.project.model.Cart;
import com.project.repository.ImageRepository;
import com.project.repository.ProductRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepo; // ---> Inject your ProductRepository
    public int NumProd = 1;
    @Override
    public Product addProductToCart(Product newProductAdeed) {
        return cartRepo.save(newProductAdeed);
    }





    
}
