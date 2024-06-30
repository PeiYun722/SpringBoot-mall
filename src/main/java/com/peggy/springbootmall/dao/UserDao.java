package com.peggy.springbootmall.dao;

import com.peggy.springbootmall.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

public interface UserDao extends CrudRepository<User, Integer> {

    User findByEmail(String email);

    User findByEmailAndPassword(String email, String password);

}
