package com.peggy.springbootmall.service.impl;

import com.peggy.springbootmall.dao.ProductDao;
import com.peggy.springbootmall.dto.ProductRequest;
import com.peggy.springbootmall.model.Product;
import com.peggy.springbootmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
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
    public Integer creatProduct(ProductRequest productRequest) {
        Product product = requestToObj(null,productRequest);
        productDao.save(product);
        return  productDao.save(product).getProductId();

    }

    @Override
    public Product updateProduct(Integer productId,ProductRequest productRequest) {
        Product product = productDao.findById(productId).orElse(null);

        if (product != null) {
            requestToObj(product,productRequest);
            productDao.save(product);
        }
        return product;
    }

    @Override
    public void deleteProduct(Integer productId) {
        productDao.deleteById(productId);
    }

    public Product requestToObj(Product product, ProductRequest productRequest){
        Date date = new Date();
        if (product == null){
            product = new Product();
            product.setCreatedDate(date);
        }
        product.setProductName(productRequest.getProductName());
        product.setCategory(productRequest.getCategory());
        product.setPrice(productRequest.getPrice());
        product.setStock(productRequest.getStock());
        product.setImageUrl(productRequest.getImageUrl());
        product.setDescription(productRequest.getDescription());
        product.setLastModifiedDate(date);
        return product;
    }
}
