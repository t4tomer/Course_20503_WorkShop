package com.project.service;

import com.project.model.Image;
import com.project.model.Product;
import com.project.repository.ImageRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {

    public Product addNewProduct(Product newProduct);

    List<Product> viewAllProduct();

    public void PrintAllProductNames();

    public Product getLastProductAdded();

    public void PrintAllProductNamesByCatagory(String catagory);

    public long getSize();

    public void deleteAll();

    public List<Product> viewAll();

    Product getProductByProductCode(String productCode);

    String getProductPriceByProductCode(String productCode);

    // ! findByProductCategory -explanantion
    /*
     * The findByProductCategory method is automatically implemented by Spring Data
     * JPA,
     * allowing you to query the database for products that match a specific
     * category.
     */

    public List<Product> getAllProductByCatagory(String category);

    void removeProductByProductCode(String productCode);

    // public void addNewProduct2(Product newProduct);

}
