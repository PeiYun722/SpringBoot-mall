package com.peggy.springbootmall.service;

import com.peggy.springbootmall.model.Product;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ProductService {

    public List<Product> findAll();
    public Product findById(Integer id);
    public void save(Product product);
    public void update(Product product);
    public void delete(Integer id);


}
