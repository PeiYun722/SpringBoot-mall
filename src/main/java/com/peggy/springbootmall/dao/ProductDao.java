package com.peggy.springbootmall.dao;

import com.peggy.springbootmall.model.Product;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


public interface ProductDao extends CrudRepository<Product, Integer> , JpaSpecificationExecutor<Product> {

    @Modifying
    @Query(value = "UPDATE Product p SET p.stock = ?1, p.lastModifiedDate = CURRENT_TIMESTAMP WHERE p.productId = ?2")
    void updateStockByProductId(Integer stock, Integer productId);

}
