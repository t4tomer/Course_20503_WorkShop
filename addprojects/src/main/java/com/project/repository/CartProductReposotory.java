package com.project.repository;

import com.project.model.CartProduct;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CartProductReposotory extends CrudRepository<CartProduct, String> {

}
