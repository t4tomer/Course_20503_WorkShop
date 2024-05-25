package com.project.repository;

import com.project.model.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends CrudRepository<Product, String> {

}
