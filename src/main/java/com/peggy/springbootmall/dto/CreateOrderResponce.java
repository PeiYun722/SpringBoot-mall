package com.peggy.springbootmall.dto;

import java.util.List;

public class CreateOrderResponce {
    private Integer orderId;
    private Integer totalPrice;
    private List<OrderItemWithProduct> orderItems;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public List<OrderItemWithProduct> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemWithProduct> orderItems) {
        this.orderItems = orderItems;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }
}
