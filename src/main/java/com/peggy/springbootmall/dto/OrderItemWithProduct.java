package com.peggy.springbootmall.dto;


import com.peggy.springbootmall.model.OrderItem;
import com.peggy.springbootmall.model.Product;

public interface OrderItemWithProduct {
    Integer getOrderItemId();
    Integer getQuantity(); //數量
    Integer getAmount();   // 此商品總價格

    Integer getProductId();
    String getProductName();
    String getImageUrl();


}
