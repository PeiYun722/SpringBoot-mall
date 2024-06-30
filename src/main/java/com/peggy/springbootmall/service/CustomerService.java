package com.peggy.springbootmall.service;


import com.peggy.springbootmall.dto.CustomerLoginRequest;
import com.peggy.springbootmall.dto.CustomerRegisterRequest;
import com.peggy.springbootmall.model.Customer;

public interface CustomerService {

    Integer register(CustomerRegisterRequest userRegisterRequest);

    Customer getCustomerById(Integer userId);

    Customer login(CustomerLoginRequest userLoginRequest);

}
