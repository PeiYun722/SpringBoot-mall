package com.peggy.springbootmall.service;

import com.peggy.springbootmall.dto.CreateOrderRequest;
import com.peggy.springbootmall.dto.OrderDetailQueryParams;
import com.peggy.springbootmall.dto.OrderItemWithProduct;
import com.peggy.springbootmall.model.OrderDetail;
import com.peggy.springbootmall.model.OrderItem;
import com.peggy.springbootmall.util.Page;

import java.util.List;

public interface OrderService {

    OrderDetail createOrder(Integer customerId,CreateOrderRequest createOrderRequest);

    List<OrderItemWithProduct> getOrderItemByOrderDetailId(Integer orderDetailId);

    Page<OrderDetail> getOrderDetails(OrderDetailQueryParams orderDetailQueryParams);
}
