package com.peggy.springbootmall.service;

import com.peggy.springbootmall.constant.ProductCategory;
import com.peggy.springbootmall.dto.ProductQueryParams;
import com.peggy.springbootmall.dto.ProductRequest;
import com.peggy.springbootmall.model.Product;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ProductService {

     List<Product> getProducts(ProductQueryParams productQueryParams);
     Product findById(Integer productId);
     Integer creatProduct(ProductRequest productRequest);
     Product updateProduct(Integer productId,ProductRequest productRequest);
     void deleteProduct(Integer productId);


}
