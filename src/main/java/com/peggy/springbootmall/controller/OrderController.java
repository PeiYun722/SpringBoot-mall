package com.peggy.springbootmall.controller;

import com.peggy.springbootmall.dto.CreateOrderRequest;
import com.peggy.springbootmall.dto.CreateOrderResponce;
import com.peggy.springbootmall.dto.OrderDetailQueryParams;
import com.peggy.springbootmall.dto.OrderItemWithProduct;
import com.peggy.springbootmall.model.OrderDetail;
import com.peggy.springbootmall.model.OrderItem;
import com.peggy.springbootmall.service.OrderService;
import com.peggy.springbootmall.util.Page;
import jakarta.persistence.criteria.Order;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    // 創建訂單
    @PostMapping("/customer/{customerId}/orderDetails")
    public ResponseEntity<CreateOrderResponce> createOrder(
                                        @PathVariable("customerId") Integer customerId,
                                        @RequestBody @Valid CreateOrderRequest createOrderRequest) {

        OrderDetail orderDetail = orderService.createOrder(customerId,createOrderRequest);
        List<OrderItemWithProduct> orderItemWithProducts= orderService.getOrderItemByOrderDetailId(orderDetail.getOrderDetailId());
        CreateOrderResponce createOrderResponce = new CreateOrderResponce();
        createOrderResponce.setOrderId(orderDetail.getOrderDetailId());
        createOrderResponce.setTotalPrice(orderDetail.getTotalAmount());
        createOrderResponce.setOrderItems(orderItemWithProducts);
        return ResponseEntity.status(HttpStatus.CREATED).body(createOrderResponce);
    }

    // 查詢訂單
    @GetMapping("/customer/{customerId}/orderDetails")
    public ResponseEntity<Page<OrderDetail>> getOrderDetails(
            @PathVariable("customerId") Integer customerId,
            //分頁
            @RequestParam(required = false,defaultValue = "200") @Min(0) Integer limit,
            @RequestParam(required = false,defaultValue = "0") @Min(0) Integer offset){
        OrderDetailQueryParams orderDetailQueryParams = new OrderDetailQueryParams();
        orderDetailQueryParams.setCustomerId(customerId);
        orderDetailQueryParams.setLimit(limit);
        orderDetailQueryParams.setOffset(offset);

        Page<OrderDetail> results = orderService.getOrderDetails(orderDetailQueryParams);

        return ResponseEntity.status(HttpStatus.OK).body(results);
    }


}
