package com.peggy.springbootmall.controller;




import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.peggy.springbootmall.constant.ProductCategory;
import com.peggy.springbootmall.dto.ProductRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;


import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void getProducts_category() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/products")
                .queryParam("productCategory","BREAD");

        MvcResult mvc = mockMvc.perform(requestBuilder)
                .andExpect(jsonPath("$.total",equalTo(5)))
                .andReturn();
    }

    @Test
    public void getProducts_name() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/products")
                .queryParam("productName","卡龍");

        MvcResult mvc = mockMvc.perform(requestBuilder)
                .andExpect(jsonPath("$.total",equalTo(1)))
                .andExpect(jsonPath("$.results[0].productId",equalTo(15)))
                .andReturn();
    }
    @Test
    public void getProducts_sort() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/products")
                .queryParam("orderBy","price")
                .queryParam("sort","desc");

        MvcResult mvc = mockMvc.perform(requestBuilder)
                .andExpect(jsonPath("$.total",equalTo(10)))
                .andExpect(jsonPath("$.results[0].productId",equalTo(6)))
                .andReturn();

    }
    @Test
    public void getProducts_page() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/products")
                .queryParam("orderBy","productId")
                .queryParam("sort","desc")
                .queryParam("limit","10")
                .queryParam("offset","3");

        MvcResult mvc = mockMvc.perform(requestBuilder)
                .andExpect(jsonPath("$.total",equalTo(10)))
                .andExpect(jsonPath("$.results[0].productId",equalTo(18)))
                .andReturn();

    }

    @Test
    public void getProduct_success() throws Exception {
        // 有id的
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/products/{id}", 3); //get方法

        MvcResult rs = mockMvc.perform(requestBuilder) //執行
                .andDo(print())// 可印出json
                .andExpect(status().is(200)) //返回要是200
                .andExpect(jsonPath("$.productName", equalTo("莓果起司蛋糕"))) //json的id返回值要是3
                .andExpect(jsonPath("$.category", equalTo("CAKE")))
                .andExpect(jsonPath("$.price", equalTo(280)))
                .andExpect(jsonPath("$.productId", equalTo(3)))
                .andReturn(); //加這個可以取得MvcResult 物件
    }
    @Test
    public void getProduct_notFound() throws Exception {
        //找不到id的
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/products/{id}", 10000); //get方法

        MvcResult rs = mockMvc.perform(requestBuilder) //執行
                .andDo(print())// 可印出json
                .andExpect(status().is(404))
                .andReturn();
    }


    @Test
    @Transactional
    public void createProduct_success() throws Exception {
        ProductRequest productRequest = new ProductRequest();
        productRequest.setProductName("檸檬派");
        productRequest.setPrice(350);
        productRequest.setCategory(ProductCategory.valueOf("DESSERT"));
        productRequest.setStock(30);
        productRequest.setDescription("好吃檸檬!");
        productRequest.setImageUrl("http://test.com");

        String content = objectMapper.writeValueAsString(productRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MvcResult mvc = mockMvc.perform(requestBuilder)
                .andExpect(status().is(201))
                .andReturn();
    }

    @Test
    @Transactional
    public void createProduct_illegalArgument() throws Exception {
        ProductRequest productRequest = new ProductRequest();
        String content = objectMapper.writeValueAsString(productRequest);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        MvcResult mvc = mockMvc.perform(requestBuilder)
                .andExpect(status().is(400))
                .andReturn();

    }

    @Test
    @Transactional
    public void updateProduct_success() throws Exception {
        ProductRequest productRequest = new ProductRequest();
        productRequest.setProductName("小餅乾");
        productRequest.setPrice(2000);
        productRequest.setCategory(ProductCategory.valueOf("COOKIE"));
        productRequest.setStock(30);
        productRequest.setDescription("更新小餅乾");
        productRequest.setImageUrl("http://test.com");
        String content = objectMapper.writeValueAsString(productRequest);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/products/{productId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        MvcResult rs = mockMvc.perform(requestBuilder)
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.productId",equalTo(1)))
                .andExpect(jsonPath("$.productName",equalTo("小餅乾")))
                .andExpect(jsonPath("$.price",equalTo(2000)))
                .andExpect(jsonPath("$.category",equalTo("COOKIE")))
                .andExpect(jsonPath("$.stock",equalTo(30)))
                .andExpect(jsonPath("$.description",equalTo("更新小餅乾")))
                .andExpect(jsonPath("$.imageUrl",equalTo("http://test.com")))
                .andReturn();
    }

    @Test
    @Transactional
    public void updateProduct_NotFound() throws Exception {
        ProductRequest productRequest = new ProductRequest();
        productRequest.setProductName("小餅乾");
        productRequest.setPrice(2000);
        productRequest.setCategory(ProductCategory.valueOf("COOKIE"));
        productRequest.setStock(30);
        productRequest.setDescription("更新小餅乾");
        productRequest.setImageUrl("http://test.com");
        String content = objectMapper.writeValueAsString(productRequest);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/products/{productId}", 10000)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        MvcResult rs = mockMvc.perform(requestBuilder)
                .andExpect(status().is(404))
                .andReturn();
    }

    @Test
    @Transactional
    public void updateProduct_illegalArgument() throws Exception {
        ProductRequest productRequest = new ProductRequest();
        String content = objectMapper.writeValueAsString(productRequest);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/products/{productId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        MvcResult mvc = mockMvc.perform(requestBuilder)
                .andExpect(status().is(400))
                .andReturn();

    }

    @Test
    @Transactional
    public void deleteProduct_success() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/products/{productId}", 10);

        MvcResult mvc = mockMvc.perform(requestBuilder)
                .andExpect(status().is(204))
                .andReturn();

    }

    @Test
    @Transactional
    public void deleteProduct_NotFound() throws Exception {

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/products/{productId}", 10000);

        MvcResult mvc = mockMvc.perform(requestBuilder)
                .andExpect(status().is(204))
                .andReturn();
    }
}