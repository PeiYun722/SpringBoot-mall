package com.peggy.springbootmall.dao;

import com.peggy.springbootmall.model.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface ProductDao extends CrudRepository<Product, Integer> {

}
