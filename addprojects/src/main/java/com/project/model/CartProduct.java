package com.project.model;

import javax.persistence.*;
import java.sql.Blob;

@Entity
@Table(name = "Cart_Table")
public class CartProduct {
    @Id
    private String emailCostomer;
    private String productCodeAdded;
    private String productNameAdded;
    private String productPriceAdded;
    private String productQuantityAdded;

    @Lob
    private Blob productImageAdded;

    public CartProduct() {
        // Default constructor for JPA
    }

    public CartProduct(String emailCostomer, Product productAdded, String productQuantityAdded) {
        this.emailCostomer = emailCostomer;
        this.productCodeAdded = productAdded.getProductCode();
        this.productNameAdded = productAdded.getProductName();
        this.productPriceAdded = productAdded.getProductPrice();
        this.productQuantityAdded = productQuantityAdded;
        this.productImageAdded = productAdded.getBlobType();
    }

    // Getters and setters
    public String getEmailCostomer() {
        return emailCostomer;
    }

    public void setEmailCostomer(String emailCostomer) {
        this.emailCostomer = emailCostomer;
    }

    public String getProductCodeAdded() {
        return productCodeAdded;
    }

    public void setProductCodeAdded(String productCodeAdded) {
        this.productCodeAdded = productCodeAdded;
    }

    public String getProductNameAdded() {
        return productNameAdded;
    }

    public void setProductNameAdded(String productNameAdded) {
        this.productNameAdded = productNameAdded;
    }

    public String getProductPriceAdded() {
        return productPriceAdded;
    }

    public void setProductPriceAdded(String productPriceAdded) {
        this.productPriceAdded = productPriceAdded;
    }

    public String getProductQuantityAdded() {
        return productQuantityAdded;
    }

    public void setProductQuantityAdded(String productQuantityAdded) {
        this.productQuantityAdded = productQuantityAdded;
    }

    public Blob getProductImageAdded() {
        return productImageAdded;
    }

    public void setProductImageAdded(Blob productImageAdded) {
        this.productImageAdded = productImageAdded;
    }
}
