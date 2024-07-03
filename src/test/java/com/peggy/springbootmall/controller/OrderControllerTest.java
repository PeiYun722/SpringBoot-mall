package com.peggy.springbootmall.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.peggy.springbootmall.dao.ProductDao;
import com.peggy.springbootmall.dto.BuyItem;
import com.peggy.springbootmall.dto.CreateOrderRequest;
import com.peggy.springbootmall.model.Product;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.SimpleFormatter;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ProductDao productDao;

    //創建訂單
    @Test
    public void createOrder_success() throws Exception {
        CreateOrderRequest createOrderRequest = getSuccessCreateOrderRequest();

        String content = objectMapper.writeValueAsString(createOrderRequest);
        //模擬request
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/customer/{customerId}/orderDetails", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        MvcResult mvc = mockMvc.perform(requestBuilder)
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.orderId",notNullValue()))
                .andExpect(jsonPath("$.totalPrice",equalTo(250+300*2)))
                .andExpect(jsonPath("$.orderItems[0].productId",equalTo(1)))
                .andExpect(jsonPath("$.orderItems[0].quantity",equalTo(1)))
                .andExpect(jsonPath("$.orderItems[0].productName",notNullValue()))
                .andExpect(jsonPath("$.orderItems[0].imageUrl",notNullValue()))
                .andExpect(jsonPath("$.orderItems[0].amount",equalTo(250)))
                .andExpect(jsonPath("$.orderItems[1].productId",equalTo(2)))
                .andExpect(jsonPath("$.orderItems[1].quantity",equalTo(2)))
                .andExpect(jsonPath("$.orderItems[1].productName",notNullValue()))
                .andExpect(jsonPath("$.orderItems[1].imageUrl",notNullValue()))
                .andExpect(jsonPath("$.orderItems[1].amount",equalTo(300*2)))
                .andReturn();

        //檢查產品更新時間有無異動

    }
    @Test
    public void createOrder_customerNotFound() throws Exception {
        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        List<BuyItem> buyItems = new ArrayList<BuyItem>();
        //第一個商品 (name = '巧克力紅蘿蔔蛋糕',url = 'https://i.imgur.com/Ik1oQQr.jpg',price = 250,stock = 10)
        BuyItem buyItem1 = new BuyItem(1,1);
        buyItems.add(buyItem1);
        //第二個商品(name = '草莓三層蛋糕', url = 'https://i.imgur.com/EBjsdO2.jpg',price = 300,stock = 10)
        BuyItem buyItem2 = new BuyItem(2,2);
        buyItems.add(buyItem2);
        createOrderRequest.setBuyItemList(buyItems);

        String content = objectMapper.writeValueAsString(createOrderRequest);
        //模擬request
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/customer/{customerId}/orderDetails", 300)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MvcResult mvc = mockMvc.perform(requestBuilder)
                .andReturn();
    }

    @Test
    public void createOrder_productNotFound() throws Exception {
        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        List<BuyItem> buyItems = new ArrayList<BuyItem>();
        BuyItem buyItem1 = new BuyItem(400,1);
        buyItems.add(buyItem1);

        String content = objectMapper.writeValueAsString(createOrderRequest);
        //模擬request
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/customer/{customerId}/orderDetails", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MvcResult mvc = mockMvc.perform(requestBuilder)
                .andExpect(status().is(400))
                .andReturn();
    }
    @Test
    public void createOrder_productNotEnough() throws Exception {
        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        List<BuyItem> buyItems = new ArrayList<BuyItem>();
        BuyItem buyItem1 = new BuyItem(1,2000);
        buyItems.add(buyItem1);

        String content = objectMapper.writeValueAsString(createOrderRequest);
        //模擬request
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/customer/{customerId}/orderDetails", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MvcResult mvc = mockMvc.perform(requestBuilder)
                .andExpect(status().is(400))
                .andReturn();
    }

    // 訂單列表查詢
    @Test
    public void getOrder_success() throws Exception {
        //先建立商品
        CreateOrderRequest createOrderRequest = getSuccessCreateOrderRequest();
        String content = objectMapper.writeValueAsString(createOrderRequest);
        RequestBuilder createRequestBuilder = MockMvcRequestBuilders
                .post("/customer/{customerId}/orderDetails", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        //模擬做四筆訂單
        for (int i = 0; i < 4; i++) {
            mockMvc.perform(createRequestBuilder);
        }

        //查詢request start
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/customer/{customerId}/orderDetails", 1)
                .queryParam("limit","2")
                .queryParam("offset","1");
        MvcResult mvc = mockMvc.perform(requestBuilder)
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.limit",equalTo(2)))
                .andExpect(jsonPath("$.offset",equalTo(1)))
                .andExpect(jsonPath("$.total",equalTo(2)))
                .andExpect(jsonPath("$.results",hasSize(2)))
                .andExpect(jsonPath("$.results[0].orderDetailId",notNullValue()))
                .andExpect(jsonPath("$.results[0].customerId",notNullValue()))
                .andExpect(jsonPath("$.results[0].totalAmount",notNullValue()))
                .andExpect(jsonPath("$.results[0].createdDate",notNullValue()))
                .andExpect(jsonPath("$.results[0].lastModifiedDate",notNullValue()))
                .andReturn();
    }
    @Test
    public void getOrder_customerNotFound() throws Exception {{
        //先建立商品
        CreateOrderRequest createOrderRequest = getSuccessCreateOrderRequest();
        String content = objectMapper.writeValueAsString(createOrderRequest);
        RequestBuilder createRequestBuilder = MockMvcRequestBuilders
                .post("/customer/{customerId}/orderDetails", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        //模擬做四筆訂單
        for (int i = 0; i < 4; i++) {
            mockMvc.perform(createRequestBuilder);
        }

        //查詢request start
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/customer/{customerId}/orderDetails", 1000)
                .queryParam("limit","2")
                .queryParam("offset","1");
        MvcResult mvc = mockMvc.perform(requestBuilder)
                .andExpect(status().is(400))
                .andReturn();
    }


    }
    //建立創建訂單的json
    private static CreateOrderRequest getSuccessCreateOrderRequest(){
        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        List<BuyItem> buyItems = new ArrayList<BuyItem>();
        //第一個商品 (name = '巧克力紅蘿蔔蛋糕',url = 'https://i.imgur.com/Ik1oQQr.jpg',price = 250,stock = 10)
        BuyItem buyItem1 = new BuyItem(1,1);
        buyItems.add(buyItem1);
        //第二個商品(name = '草莓三層蛋糕', url = 'https://i.imgur.com/EBjsdO2.jpg',price = 300,stock = 10)
        BuyItem buyItem2 = new BuyItem(2,2);
        buyItems.add(buyItem2);
        createOrderRequest.setBuyItemList(buyItems);
        return createOrderRequest;
    }


}