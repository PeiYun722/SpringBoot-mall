package com.peggy.springbootmall.dao;

import com.peggy.springbootmall.constant.ProductCategory;
import com.peggy.springbootmall.model.Product;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Vector;

public interface ProductDao extends CrudRepository<Product, Integer> , JpaSpecificationExecutor<Product> {


}
