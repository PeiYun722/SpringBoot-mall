package com.peggy.springbootmall.controller;

import com.peggy.springbootmall.constant.ProductCategory;
import com.peggy.springbootmall.dto.ProductQueryParams;
import com.peggy.springbootmall.dto.ProductRequest;
import com.peggy.springbootmall.model.Product;
import com.peggy.springbootmall.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    @Autowired
    public ProductService productService;

    //查詢商品列表
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProducts(
            @RequestParam(required = false) ProductCategory productCategory,
            @RequestParam(required = false) String productName,
            // 排序
            @RequestParam(required = false) String orderBy,
            @RequestParam(required = false, defaultValue = "asc") String sort,
            //分頁
            @RequestParam(required = false, defaultValue = "5") @Max(100) @Min(0) Integer limit,
            @RequestParam(required = false, defaultValue = "0") @Min(0) Integer offset) {
        ProductQueryParams productQueryParams = new ProductQueryParams();
        productQueryParams.setProductCategory(productCategory);
        productQueryParams.setProductName(productName);
        productQueryParams.setOrderBy(orderBy);
        productQueryParams.setSort(sort);
        productQueryParams.setLimit(limit);
        productQueryParams.setOffset(offset);

        List<Product> products = productService.getProducts(productQueryParams);

        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

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

    // 刪除商品
    @DeleteMapping("/products/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer productId){
        productService.deleteProduct(productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
