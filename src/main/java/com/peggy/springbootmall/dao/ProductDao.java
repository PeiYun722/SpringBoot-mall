package com.peggy.springbootmall.dao;

import com.peggy.springbootmall.model.Product;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface ProductDao extends CrudRepository<Product, Integer> , JpaSpecificationExecutor<Product> {


}
