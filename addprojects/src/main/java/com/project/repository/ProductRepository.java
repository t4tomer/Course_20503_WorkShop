package com.project.repository;

import com.project.model.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends CrudRepository<Product, String> {
    
    List<Product> findByProductCategory(String productCategory);

    Product findByProductCode(String productCode);

    void deleteByProductCode(String productCode);


}
