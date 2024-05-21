package com.project.service;

import com.project.model.Image;
import com.project.model.Product;
import com.project.repository.ImageRepository;
import com.project.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Arrays;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepo; // ---> Inject your ProductRepository
    public int NumProd = 1;

    @Autowired
    private ImageService ImageServiceByCategory;// used to store the images of image spesefic catagpry

    @Override // this method must be aded in the ProductRepostory&ProductService
    public Product getProductByProductCode(String productCode) {
        return productRepo.findByProductCode(productCode);
    }

    @Override
    public List<Image> viewAllImageCategory() {
        @SuppressWarnings("unchecked")
        List<Image> images = (List<Image>) ((Product) productRepo.findAll()).getBlobType();
        return images;
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

    public List<Image> getAllProductImagesByCatagory(String SerachCategory) {
        List<Product> productsListByCatagory = productRepo.findByProductCategory(SerachCategory);

        ImageServiceByCategory.deleteAll();// delete Image Service images, becouse Image Service is singleton

        for (Product product : productsListByCatagory) {
            // create new Image object and add it to ImageServiceByCategory
            Image image = new Image();
            image.setImage(product.getBlobType());
            ImageServiceByCategory.create(image);
        }

        List<Image> imageListByCategory = ImageServiceByCategory.viewAll();
        return imageListByCategory;
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
    public void addNewProduct2(Product newProduct) {
        // return productRepo.save(newProduct);
        productRepo.saveAll(Arrays.asList(newProduct));
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
