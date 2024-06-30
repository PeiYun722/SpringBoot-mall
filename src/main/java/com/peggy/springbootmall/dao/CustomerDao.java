package com.peggy.springbootmall.dao;

import com.peggy.springbootmall.model.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerDao extends CrudRepository<Customer, Integer> {

    Customer findByEmail(String email);

    Customer findByEmailAndPassword(String email, String password);

}
