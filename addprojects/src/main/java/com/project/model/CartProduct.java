package com.project.model;

// code that works 
import javax.persistence.*;
import java.io.Serializable;
import java.sql.Blob;
import lombok.Data;
import lombok.NoArgsConstructor;
// @Entity
// @Table(name = "Cart_Table")

@Entity
@Table(name = "Cart_Table", schema = "oper")
@Data
// @NoArgsConstructor
@IdClass(CartProductId.class)
public class CartProduct {
    @Id
    private String emailCustomer = "";// mail of the costomer
    @Id
    private String productCodeInCart = ""; // code of the product that added to the cart
    private String productNameInCart; // product name of the product that added to the cart
    private String productPriceInCart; // price of the product that is added to the cart
    private String productQuantityInCart; // quantity of the product that is added to the cart

    @Lob
    private Blob productImageInCart;

    public CartProduct() {
        // Default constructor for JPA
    }

    public CartProduct(String emailCustomer, Product productAdded, String productQuantityInCart) {
        this.emailCustomer = emailCustomer;
        this.productCodeInCart = productAdded.getProductCode();
        this.productNameInCart = productAdded.getProductName();
        this.productPriceInCart = productAdded.getProductPrice();
        this.productQuantityInCart = productQuantityInCart;
        this.productImageInCart = productAdded.getBlobType();
    }

    public String getEmailCostomer() {
        return emailCustomer;
    }

    public void setEmailCostomer(String emailCostomer) {
        this.emailCustomer = emailCostomer;
    }

    public String getproductCodeInCart() {
        return productCodeInCart;
    }

    public void setProductCodeInCart(String productCodeInCart) {
        this.productCodeInCart = productCodeInCart;
    }

    public String getProductNameInCart() {
        return productNameInCart;
    }

    public void setproductNameInCart(String productNameInCart) {
        this.productNameInCart = productNameInCart;
    }

    public String getProductPriceInCart() {
        return productPriceInCart;
    }

    public void setProductPriceInCart(String productPriceInCart) {
        this.productPriceInCart = productPriceInCart;
    }

    public String getProductQuantityInCart() {
        return productQuantityInCart;
    }

    public void setProductQuantityInCart(String productQuantityInCart) {
        this.productQuantityInCart = productQuantityInCart;
    }

    public Blob getproductImageInCart() {
        return productImageInCart;
    }

    public void setproductImageInCart(Blob productImageInCart) {
        this.productImageInCart = productImageInCart;
    }
}