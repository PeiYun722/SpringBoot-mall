package com.peggy.springbootmall.service.impl;

import com.peggy.springbootmall.dao.CustomerDao;
import com.peggy.springbootmall.dto.CustomerLoginRequest;
import com.peggy.springbootmall.dto.CustomerRegisterRequest;
import com.peggy.springbootmall.model.Customer;
import com.peggy.springbootmall.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;

@Component
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerDao customerDao;

    private final static Logger log = LoggerFactory.getLogger(CustomerServiceImpl.class);

    @Override
    public Integer register(CustomerRegisterRequest customerRegisterRequest) {
        // 檢查註冊的email
        Customer customer = customerDao.findByEmail(customerRegisterRequest.getEmail());
        if (customer != null) {
            log.warn("該email {} 已被{}註冊", customerRegisterRequest.getEmail(),customer.getCustomerId());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        //註冊帳號
        Date date = new Date();
        customer = new Customer();
        //使用MD5，產生密碼雜湊值
        String hashedPassword = DigestUtils.md5DigestAsHex(customerRegisterRequest.getPassword().getBytes());
        customer.setPassword(hashedPassword);
        customer.setEmail(customerRegisterRequest.getEmail());
        customer.setCreatedDate(date);
        customer.setLastModifiedDate(date);
        customer = customerDao.save(customer);
        return customer.getCustomerId();
    }

    @Override
    public Customer getCustomerById(Integer customerId) {
        return customerDao.findById(customerId).orElse(null);
    }

    @Override
    public Customer login(CustomerLoginRequest customerLoginRequest) {
        Customer customer = customerDao.findByEmail(customerLoginRequest.getEmail());
        if (customer == null) {
            log.warn("該email {}尚未註冊", customerLoginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        //使用MD5，產生密碼雜湊值
        String md5Password = DigestUtils.md5DigestAsHex(customerLoginRequest.getPassword().getBytes());
        if (customer.getPassword().equals(md5Password)) {
            return customer;
        }else {
            log.warn("email {}密碼不正確", customerLoginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
