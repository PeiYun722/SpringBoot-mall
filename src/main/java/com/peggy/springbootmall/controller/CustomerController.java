package com.peggy.springbootmall.controller;

import com.peggy.springbootmall.dto.CustomerLoginRequest;
import com.peggy.springbootmall.dto.CustomerRegisterRequest;
import com.peggy.springbootmall.model.Customer;
import com.peggy.springbootmall.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/customer/register")
    public ResponseEntity<Customer> register(@RequestBody @Valid CustomerRegisterRequest customerRegisterRequest){
        Integer customerId = customerService.register(customerRegisterRequest);
        Customer customer = customerService.getCustomerById(customerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(customer);
    }

    @PostMapping("/customer/login")
    public ResponseEntity<Customer> login(@RequestBody @Valid CustomerLoginRequest customerLoginRequest){
        Customer customer = customerService.login(customerLoginRequest);
        return ResponseEntity.status(HttpStatus.OK).body(customer);
    }
}
