package com.project.service;

import com.project.model.Product;
import com.project.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CartService {
    public Product addProductToCart(Product newProductAdeed);
}
