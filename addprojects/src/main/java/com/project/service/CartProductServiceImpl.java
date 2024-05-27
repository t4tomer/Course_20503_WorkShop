package com.project.service;

import com.project.model.Product;
import com.project.repository.CartProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.project.model.Image;
import com.project.model.CartProduct;
import com.project.repository.ImageRepository;
import com.project.repository.ProductRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartProductServiceImpl implements CartProductService {

    @Autowired
    private CartProductRepository cartRepo; // ---> Inject your ProductRepository
    public int NumProd = 1;

    @Override
    public CartProduct addProductToCart(CartProduct newProductAdeed) {
        return cartRepo.save(newProductAdeed);
    }

    @Override
    public List<CartProduct> viewAll() {
        return (List<CartProduct>) cartRepo.findAll();
    }

    // return list of the products of the same SerachCategory
    public List<CartProduct> getAllProductsInCartOfUser(String clientEmail) {
        List<CartProduct> productsListCartByUser = cartRepo.findByEmailCustomer(clientEmail);
        return productsListCartByUser;
    }

}
