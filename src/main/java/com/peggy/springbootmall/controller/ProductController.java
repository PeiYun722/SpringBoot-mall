package com.peggy.springbootmall.controller;

import com.peggy.springbootmall.dto.ProductRequest;
import com.peggy.springbootmall.model.Product;
import com.peggy.springbootmall.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProductController {

    @Autowired
    public ProductService productService;

    // 根據ID取得商品
    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Integer id) {
        Product product = productService.findById(id);
        if (product != null) {
            return ResponseEntity.status(HttpStatus.OK).body(product);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }
    // 新增商品
    @PostMapping("/products")
    public ResponseEntity<Integer> createProduct(@RequestBody @Valid ProductRequest productRequest) throws Exception {
        Integer productId = productService.creatProduct(productRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(productId);
    }
    // 修改商品
    @PutMapping("/products/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer productId ,
                                                 @RequestBody @Valid ProductRequest productRequest) throws Exception {

        Product product = productService.updateProduct(productId,productRequest);
        if(product == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer productId){
        productService.deleteProduct(productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
