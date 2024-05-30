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

    @Autowired
    private ProductService productService;

    public int NumProd = 1;


    @Override //used to update the quantity of products after cheack out of the user
    public void updateQuantityInProductTable(List<CartProduct> productInCart) {

        for (CartProduct cartProduct : productInCart) {
            String productCode = cartProduct.getproductCodeInCart();
            String productQuantityInCart = cartProduct.getProductQuantityInCart();
            Product productObject = productService.getProductByProductCode(productCode);
            String productQuantity = productObject.getProductQuantity();
            int newQunatity = Integer.parseInt(productQuantity) - Integer.parseInt(productQuantityInCart);
            String newQunatityStr = newQunatity + "";
            productObject.setProductQuantity(newQunatityStr);
            productService.addNewProduct(productObject);
        }
    }

    @Override
    public CartProduct addProductToCart(CartProduct newProductAdeed) {
        return cartRepo.save(newProductAdeed);
    }

    @Override
    public List<CartProduct> viewAll() {
        return (List<CartProduct>) cartRepo.findAll();
    }

    public void deleteAll() { // delete all the values in the product repository
        cartRepo.deleteAll();
    }

    // return list of the products of the same SerachCategory
    public List<CartProduct> getAllProductsInCartOfUser(String clientEmail) {
        List<CartProduct> productsListCartByUser = cartRepo.findByEmailCustomer(clientEmail);
        return productsListCartByUser;
    }

    public int getSubTotal(List<CartProduct> productsInCart) {
        // List<CartProduct> ListProductsInCart = viewAll();
        int subTotal = 0;
        // Iterate over each CartProduct object
        for (CartProduct cartProduct : productsInCart) {
            // Access the product name using getProduct().getProductName() method
            String productQuantity = cartProduct.getProductQuantityInCart();
            String productPrice = cartProduct.getProductPriceInCart();
            subTotal = subTotal + Integer.parseInt(productPrice) * Integer.parseInt(productQuantity);
        }
        System.out.println("SubTotal: " + subTotal);
        return subTotal;

    }

    // Method to remove a product by its code from the cart
    public void removeProductFromCart(CartProduct p1) {
        cartRepo.delete(p1);
    }

}
