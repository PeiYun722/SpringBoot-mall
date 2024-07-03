package com.peggy.springbootmall.dao;


import com.peggy.springbootmall.dto.OrderItemWithProduct;
import com.peggy.springbootmall.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Map;

public interface OrderItemDao extends JpaRepository<OrderItem, Integer> {

    public List<OrderItem> findByOrderDetailId(Integer orderDetailId);

    @Query(value = "SELECT p.product_Name, p.image_Url, oi.* " +
            "FROM Order_Item oi " +
            "JOIN Product p ON oi.product_Id = p.product_Id " +
            "WHERE oi.order_Detail_Id = ?1",
            nativeQuery = true)
    public List<OrderItemWithProduct> getOrderItemWithProduct(Integer orderDetailId);
//@Query(value = "SELECT new com.peggy.springbootmall.dto.OrderItemWithProduct(oi, p) " +
//        "FROM OrderItem oi " +
//        "JOIN oi.product p " +  // 假设 OrderItem 中有一个 product 属性引用 Product 实体
//        "WHERE oi.orderDetailId = ?1")
//public List<OrderItemWithProduct> getOrderItemWithProduct(Integer orderDetailId);

}
