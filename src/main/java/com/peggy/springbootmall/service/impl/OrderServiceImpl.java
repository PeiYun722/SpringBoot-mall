package com.peggy.springbootmall.service.impl;

import com.peggy.springbootmall.dao.CustomerDao;
import com.peggy.springbootmall.dao.OrderDetailDao;
import com.peggy.springbootmall.dao.OrderItemDao;
import com.peggy.springbootmall.dao.ProductDao;
import com.peggy.springbootmall.dto.BuyItem;
import com.peggy.springbootmall.dto.CreateOrderRequest;
import com.peggy.springbootmall.dto.OrderDetailQueryParams;
import com.peggy.springbootmall.dto.OrderItemWithProduct;
import com.peggy.springbootmall.model.Customer;
import com.peggy.springbootmall.model.OrderDetail;
import com.peggy.springbootmall.model.OrderItem;
import com.peggy.springbootmall.model.Product;
import com.peggy.springbootmall.service.OrderService;
import com.peggy.springbootmall.util.Page;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderItemDao orderItemDao;
    @Autowired
    private OrderDetailDao orderDetailDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private CustomerDao customerDao;

    @PersistenceContext
    private EntityManager entityManager;


    private final static Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Transactional
    @Override
    public OrderDetail createOrder(Integer customerId ,CreateOrderRequest createOrderRequest) {
        //檢查costomer是否存在
        Customer customer = customerIsExists(customerId);

        // 創建訂單orderDatail
        Integer totalPrice = 0;
        Integer eachPrice;
        List<OrderItem> orderItems = new ArrayList<>();
        // 創立orderItem 及計算總價格
        for (BuyItem buyItem : createOrderRequest.getBuyItemList()) {
            OrderItem orderItem = new OrderItem();
            eachPrice = 0;
            Product product = productDao.findById(buyItem.getProductId()).orElse(null);
            if(product == null){
                log.warn("該商品 {} 不存在",buyItem.getProductId());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }else if (product.getStock() < buyItem.getQuantity()) {
                log.warn("該商品 {} 庫存數量不足，無法購買。剩餘庫存{}，欲購買的數量{}",
                        buyItem.getProductId(), product.getStock(), buyItem.getQuantity());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }else{
                // 更新商品數量
                productDao.updateStockByProductId(product.getStock()-buyItem.getQuantity(),product.getProductId());
                eachPrice = product.getPrice() * buyItem.getQuantity();
                totalPrice = totalPrice + eachPrice ;
                orderItem.setProductId(product.getProductId());
                orderItem.setQuantity(buyItem.getQuantity());
                orderItem.setAmount(eachPrice);
                orderItems.add(orderItem);
            }

        }
        Date now = new Date();
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setCustomerId(customerId);
        orderDetail.setCreatedDate(now);
        orderDetail.setLastModifiedDate(now);
        orderDetail.setTotalAmount(totalPrice);
        orderDetail = orderDetailDao.save(orderDetail);
        for(OrderItem orderItem : orderItems){
            orderItem.setOrderDetailId(orderDetail.getOrderDetailId());
        }
        orderItemDao.saveAll(orderItems);

        return orderDetail;
    }
    public List<OrderItemWithProduct> getOrderItemByOrderDetailId(Integer orderDetailId){
        List<OrderItemWithProduct> info = orderItemDao.getOrderItemWithProduct(orderDetailId);
        return info;
    }

    @Override
    public Page<OrderDetail> getOrderDetails(OrderDetailQueryParams orderDetailQueryParams) {
        //檢查costomer是否存在
        Customer customer = customerIsExists(orderDetailQueryParams.getCustomerId());

        // 查詢
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<OrderDetail> query = criteriaBuilder.createQuery(OrderDetail.class);
        Root<OrderDetail> root = query.from(OrderDetail.class);
        List<Predicate> predicates = new ArrayList<>();
        // where customerId
        predicates.add(criteriaBuilder.equal(root.get("customerId"),orderDetailQueryParams.getCustomerId()));
        query.where(predicates.toArray(new Predicate[0]));

        List<OrderDetail> orderDetails = entityManager.createQuery(query)
            .setFirstResult(orderDetailQueryParams.getOffset()!= null ?orderDetailQueryParams.getOffset():0)
            .setMaxResults(orderDetailQueryParams.getLimit()!= null ?orderDetailQueryParams.getLimit():200)
            .getResultList();
        //返回
        Page<OrderDetail> result = new Page<>();
        result.setLimit(orderDetailQueryParams.getLimit());
        result.setOffset(orderDetailQueryParams.getOffset());
        result.setTotal(orderDetails.size());
        result.setResults(orderDetails);
        return result;
    }

    // 檢查customer 存不存在
    private Customer customerIsExists(Integer customerId){
        Customer customer = customerDao.findById(customerId).orElse(null);
        if(customer == null){
            log.warn("該Customer {} 不存在",customerId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return customer;
    }


}
