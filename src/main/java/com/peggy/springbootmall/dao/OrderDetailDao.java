package com.peggy.springbootmall.dao;

import com.peggy.springbootmall.model.OrderDetail;
import org.springframework.data.repository.CrudRepository;

public interface OrderDetailDao  extends CrudRepository<OrderDetail, Integer> {

}
