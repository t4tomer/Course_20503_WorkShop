package com.project.model;

import javax.persistence.*;
import java.util.Date;
import java.sql.Blob;
import com.project.model.Image;

@Entity
//create table called Product_Table in the Project_Data_Base
@Table(name = "Product_Table") 
public class Product {
    @Id
    private String productCode;// primary key of the product db
    private String productName;// name of the product
    private String productPrice;//  price of one product
    private String productQuantity; //  quantity of the product
    private Date productDate;// the date the product was uploaded to the website
    @Lob
    private Blob productImage;// image of the product

    private String productCategory;// The category of the product

    public Product() {
        System.out.println("new Product Object created ");
    }

    public Product(
            String productCode,
            String productName,
            String productPrice,
            String productQuantity,
            String productCategory,
            Date productDate,
            Blob productImage) {
        this.productCode = productCode;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.productCategory = productCategory;
        this.productDate = productDate;
        this.productImage = productImage;

        System.out.println("New Product Object created with values ");
    }

    // Getters and setters for productCode
    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    // Getters and setters for productName
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    // Getters and setters for productPrice
    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    // Getters and setters for productQuantity
    public String getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
    }

    // Getters and setters for productDate
    public Date getProductDate() {
        return productDate;
    }

    public void setProductDate(Date productDate) {
        this.productDate = productDate;
    }

    // Getters and setters for productImage
    public Blob getBlobType() {
        return productImage;
    }

    public void setProductImage(Blob productImage) {
        this.productImage = productImage;
    }

    // Getters and setters for productCategory
    public String getproductCategory() {
        return productCategory;
    }

    public void setproductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productCode='" + productCode + '\'' +
                ", productName='" + productName + '\'' +
                ", productPrice=" + productPrice +
                ", productQuantity=" + productQuantity +
                ", productCategory='" + productCategory + '\'' +
                ", addted to site at:=" + productDate +
                '}';
    }

}
