package com.project.model;

import java.util.Random;
import javax.persistence.*;

import java.sql.Blob;
import java.util.Date;
import java.util.List;
import com.project.model.Product;

@Entity
@Table(name = "Cart_Table")
public class CartProduct {
    @Id
    private String emailCostomer;
    private String productCodeAdded;// code of the product that added to the cart
    private String productNameAdded;// product name of the product that added to the cart

    private String productPriceAdded;// price of the product that is added to the cart
    private String productQuantityAdded; // quantity of the product that is added to the cart

    // @Lob
    // private Image productImageAdded;

    public CartProduct(String emailCostomer, Product productAdded, String productQuantityAdded) {
        this.emailCostomer = emailCostomer;
        this.productCodeAdded = productAdded.getProductCode();
        this.productNameAdded = productAdded.getProductName();
        this.productPriceAdded = productAdded.getProductPrice();
        this.productQuantityAdded = productQuantityAdded;
        // this.productImageAdded = productImageAdded.getBlobType();
    }

    public void setEmailCostomer(String emailCostomer) {
        this.emailCostomer = emailCostomer;
    }

    public String getCustomerEmail() {
        return emailCostomer;
    }

}
