package com.peggy.springbootmall.service.impl;

import com.peggy.springbootmall.dao.UserDao;
import com.peggy.springbootmall.dto.UserLoginRequest;
import com.peggy.springbootmall.dto.UserRegisterRequest;
import com.peggy.springbootmall.model.User;
import com.peggy.springbootmall.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;

@Component
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    private final static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public Integer register(UserRegisterRequest userRegisterRequest) {
        // 檢查註冊的email
        User user = userDao.findByEmail(userRegisterRequest.getEmail());
        if (user != null) {
            log.warn("該email {} 已被{}註冊", userRegisterRequest.getEmail(),user.getUserId());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        //註冊帳號
        Date date = new Date();
        user = new User();
        //使用MD5，產生密碼雜湊值
        String hashedPassword = DigestUtils.md5DigestAsHex(userRegisterRequest.getPassword().getBytes());
        user.setPassword(hashedPassword);
        user.setEmail(userRegisterRequest.getEmail());
        user.setCreatedDate(date);
        user.setLastModifiedDate(date);
        user = userDao.save(user);
        return user.getUserId();
    }

    @Override
    public User getUserById(Integer userId) {
        return userDao.findById(userId).orElse(null);
    }

    @Override
    public User login(UserLoginRequest userLoginRequest) {
        User user = userDao.findByEmail(userLoginRequest.getEmail());
        if (user == null) {
            log.warn("該email {}尚未註冊", userLoginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        //使用MD5，產生密碼雜湊值
        String md5Password = DigestUtils.md5DigestAsHex(userLoginRequest.getPassword().getBytes());
        if (user.getPassword().equals(md5Password)) {
            return user;
        }else {
            log.warn("email {}密碼不正確", userLoginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
