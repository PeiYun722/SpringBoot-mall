package com.peggy.springbootmall.service;

import com.peggy.springbootmall.dto.UserLoginRequest;
import com.peggy.springbootmall.dto.UserRegisterRequest;
import com.peggy.springbootmall.model.User;

public interface UserService {

    Integer register(UserRegisterRequest userRegisterRequest);

    User getUserById(Integer userId);

    User login(UserLoginRequest userLoginRequest);

}
