package com.project.service;

import com.project.model.Product;
import com.project.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    public int convertToInt(String num) {
        return Integer.parseInt(num);
    }

    @Autowired
    private ProductRepository productRepo; // ---> Inject your ProductRepository
    public int NumProd = 1;

    @Override
    public List<Product> removeZeroQunantityProducts(List<Product> productListByCategory) {
        for (Product product : productListByCategory) {
            System.out.println("\t-->Product quality test!!!");
            if (convertToInt(product.getProductQuantity()) == 0) {
                System.out.println("\t-->Product quality is zero");
                String idProduct = product.getProductCode();
                productRepo.deleteByProductCode(idProduct);
            }
        }
        return productListByCategory;
    }

    @Override // this method must be aded in the ProductRepostory&ProductService
    public Product getProductByProductCode(String productCode) {
        return productRepo.findByProductCode(productCode);
    }

    @Override // this method must be aded in the ProductRepostory&ProductService
    public String getProductPriceByProductCode(String productCode) {
        Product p1 = productRepo.findByProductCode(productCode);
        String productPrice = p1.getProductPrice();
        return productPrice;
    }

    @Override
    public void removeProductByProductCode(String productCode) {
        System.out.println("\t\t-->product" + productCode + " has been removed");
        productRepo.deleteByProductCode(productCode);
    }

    public void deleteAll() { // delete all the values in the product repository
        productRepo.deleteAll();
    }

    // return list of the products of the same SerachCategory
    public List<Product> getAllProductByCatagory(String SerachCategory) {
        List<Product> productsListByCatagory = productRepo.findByProductCategory(SerachCategory);
        return productsListByCatagory;
    }

    @Override
    public List<Product> viewAll() {
        return (List<Product>) productRepo.findAll();
    }

    @Override
    public void PrintAllProductNames() {
        List<Product> products = (List<Product>) productRepo.findAll();
        NumProd = 1;
        for (Product product : products) {
            System.out.println("prdouct number:" + NumProd + " is called:" + product.getProductName());
            NumProd++;
        }
    }

    public void PrintAllProductNamesByCatagory(String catagory) {
        List<Product> productsList = (List<Product>) productRepo.findAll();
        System.out.println("The prdocut names is(in all of prefumes is):");

        for (Product product : productsList) {
            if (product.getproductCategory().equals(catagory))
                System.out.println("\t\t-->Prefume Product name is :" + product.getProductName());
        }
    }

    public Product getLastProductAdded() {
        List<Product> productsList = (List<Product>) productRepo.findAll(); // Assuming productService.findAll() fetches
                                                                            // all products
        Product lastProduct = null;
        if (!productsList.isEmpty()) {
            lastProduct = productsList.get(productsList.size() - 1); // Get the last product
            System.out.println("Last Product: " + lastProduct); // For debugging
        } else {
            System.out.println("Product list is empty.");
        }
        return lastProduct;
    }

    @Override
    public Product addNewProduct(Product newProduct) {
        return productRepo.save(newProduct);
    }

    @Override
    public List<Product> viewAllProduct() {
        return (List<Product>) productRepo.findAll();
    }

    // New method to get the size of the product repository
    public long getSize() {
        return productRepo.count();
    }

}
