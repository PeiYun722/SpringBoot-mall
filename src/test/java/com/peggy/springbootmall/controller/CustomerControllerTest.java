package com.peggy.springbootmall.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.peggy.springbootmall.dao.CustomerDao;
import com.peggy.springbootmall.dto.CustomerLoginRequest;
import com.peggy.springbootmall.dto.CustomerRegisterRequest;
import com.peggy.springbootmall.model.Customer;

import com.peggy.springbootmall.service.CustomerService;
import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CustomerControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CustomerDao customerDao;
    @Autowired
    private CustomerService customerService;
    // 註冊新帳號
    @Test
    public void register_success() throws Exception {
        CustomerRegisterRequest customerRegisterRequest = new CustomerRegisterRequest();
        customerRegisterRequest.setEmail("w123@gmail.com");
        customerRegisterRequest.setPassword("123456");
        String content = objectMapper.writeValueAsString(customerRegisterRequest);
        //模擬request
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/customer/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        MvcResult mvc = mockMvc.perform(requestBuilder)
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.customerId",notNullValue()))
                .andExpect(jsonPath("$.email",notNullValue()))
//                .andExpect(jsonPath("$.password",notNullValue())) //不能返回
                .andExpect(jsonPath("$.createdDate",notNullValue()))
                .andExpect(jsonPath("$.lastModifiedDate",notNullValue()))
                .andReturn();

        //檢查資料庫密碼不為明碼
        Customer customer = customerDao.findByEmail(customerRegisterRequest.getEmail());
        assertNotEquals(customerRegisterRequest.getPassword(),customer.getPassword());
    }
    // email 格式不正確
    @Test
    public void register_emailInvalid() throws Exception {
        CustomerRegisterRequest customerRegisterRequest = new CustomerRegisterRequest();
        customerRegisterRequest.setEmail("w123gmail.com");
        customerRegisterRequest.setPassword("123456");
        String content = objectMapper.writeValueAsString(customerRegisterRequest);
        //模擬request
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/customer/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400))
                .andReturn();
    }
    // email 已存在
    @Test
    public void register_emailAlreadyExist() throws Exception {
        CustomerRegisterRequest customerRegisterRequest = new CustomerRegisterRequest();
        customerRegisterRequest.setEmail("w123@gmail.com");
        customerRegisterRequest.setPassword("123456");
        String content = objectMapper.writeValueAsString(customerRegisterRequest);
        //模擬request
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/customer/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MvcResult mvc = mockMvc.perform(requestBuilder)
                .andExpect(status().is(201))
                .andReturn();
        // 用同一組再去註冊一個帳號
        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400))
                .andReturn();
    }
    // 登入
    @Test
    public void login_success() throws Exception {
        //先註冊
        CustomerRegisterRequest customerRegisterRequest = new CustomerRegisterRequest();
        customerRegisterRequest.setEmail("w123@gmail.com");
        customerRegisterRequest.setPassword("123456");
        customerService.register(customerRegisterRequest);

        //登入
        CustomerLoginRequest customerLoginRequest = new CustomerLoginRequest();
        customerLoginRequest.setEmail("w123@gmail.com");
        customerLoginRequest.setPassword("123456");

        String content = objectMapper.writeValueAsString(customerLoginRequest);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/customer/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        MvcResult mvc = mockMvc.perform(requestBuilder)
                .andExpect(jsonPath("$.customerId", notNullValue()))
                .andExpect(jsonPath("$.email", notNullValue()))
                .andExpect(jsonPath("$.createdDate", notNullValue()))
                .andExpect(jsonPath("$.lastModifiedDate", notNullValue()))
                .andExpect(status().is(200))
                .andReturn();

    }
    // 密碼錯誤
    @Test
    public void login_wrongPassword() throws Exception {
        //先註冊
        CustomerRegisterRequest customerRegisterRequest = new CustomerRegisterRequest();
        customerRegisterRequest.setEmail("w123@gmail.com");
        customerRegisterRequest.setPassword("123456");
        customerService.register(customerRegisterRequest);

        //登入
        CustomerLoginRequest customerLoginRequest = new CustomerLoginRequest();
        customerLoginRequest.setEmail("w123@gmail.com");
        customerLoginRequest.setPassword("1234567");

        String content = objectMapper.writeValueAsString(customerLoginRequest);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/customer/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        MvcResult mvc = mockMvc.perform(requestBuilder)
                .andExpect(status().is(400))
                .andReturn();

    }

    // email 格式不正確
    @Test
    public void login_emailInvalid() throws Exception {
        //先註冊
        CustomerRegisterRequest customerRegisterRequest = new CustomerRegisterRequest();
        customerRegisterRequest.setEmail("w123@gmail.com");
        customerRegisterRequest.setPassword("123456");
        customerService.register(customerRegisterRequest);

        //登入
        CustomerLoginRequest customerLoginRequest = new CustomerLoginRequest();
        customerLoginRequest.setEmail("w123gmail.com");
        customerLoginRequest.setPassword("123456");

        String content = objectMapper.writeValueAsString(customerLoginRequest);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/customer/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        MvcResult mvc = mockMvc.perform(requestBuilder)
                .andExpect(status().is(400))
                .andReturn();

    }

    // email 找不到
    @Test
    public void login_emailNotExists() throws Exception {
        //先註冊
        CustomerRegisterRequest customerRegisterRequest = new CustomerRegisterRequest();
        customerRegisterRequest.setEmail("w123@gmail.com");
        customerRegisterRequest.setPassword("123456");
        customerService.register(customerRegisterRequest);

        //登入
        CustomerLoginRequest customerLoginRequest = new CustomerLoginRequest();
        customerLoginRequest.setEmail("w123ss@gmail.com");
        customerLoginRequest.setPassword("123456");

        String content = objectMapper.writeValueAsString(customerLoginRequest);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/customer/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        MvcResult mvc = mockMvc.perform(requestBuilder)
                .andExpect(status().is(400))
                .andReturn();

    }

    private void register(CustomerRegisterRequest customerRegisterRequest){
        Integer customer = customerService.register(customerRegisterRequest);
    }


}