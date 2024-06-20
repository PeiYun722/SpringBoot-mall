package com.peggy.springbootmall.service.impl;

import com.peggy.springbootmall.dao.ProductDao;
import com.peggy.springbootmall.model.Product;
import com.peggy.springbootmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    @Override
    public List<Product> findAll() {
        return List.of();
    }

    // 尋找單個商品
    @Override
    public Product findById(Integer id) {
        return productDao.findById(id).orElse(null);
    }

    @Override
    public void save(Product product) {

    }

    @Override
    public void update(Product product) {

    }

    @Override
    public void delete(Integer id) {

    }
}
