package com.peggy.springbootmall.service.impl;

import com.peggy.springbootmall.dao.ProductDao;
import com.peggy.springbootmall.dto.ProductQueryParams;
import com.peggy.springbootmall.dto.ProductRequest;
import com.peggy.springbootmall.model.Product;
import com.peggy.springbootmall.service.ProductService;
import io.micrometer.common.util.StringUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Product> getProducts(ProductQueryParams productQueryParams) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> query = criteriaBuilder.createQuery(Product.class);
        Root<Product> root = query.from(Product.class);

        List<Predicate> predicates = new ArrayList<>();
            //  category
            if (productQueryParams.getProductCategory()!=null) {
                predicates.add(criteriaBuilder.equal(root.get("category"), productQueryParams.getProductCategory()));
            }

            // productName
            if (!StringUtils.isBlank(productQueryParams.getProductName())) {
                System.out.println(productQueryParams.getProductName());
                predicates.add(criteriaBuilder.like(root.get("productName"), "%" + productQueryParams.getProductName() + "%"));
            }

            // 构建查询条件
            query.where(predicates.toArray(new Predicate[0]));

            // order by
            if (!StringUtils.isBlank(productQueryParams.getOrderBy())) {
                Path<Object> orderPath = root.get(productQueryParams.getOrderBy());
                if ("desc".equalsIgnoreCase(productQueryParams.getSort())) {
                    query.orderBy(criteriaBuilder.desc(orderPath));
                } else {
                    query.orderBy(criteriaBuilder.asc(orderPath));
                }
            }

            //分頁
            List<Product> products = entityManager.createQuery(query)
                    .setFirstResult(productQueryParams.getOffset() != null ? productQueryParams.getOffset() : 0)
                    .setMaxResults(productQueryParams.getLimit() != null ? productQueryParams.getLimit() : 10)
                    .getResultList();


        return products;
    }

    // 尋找單個商品
    @Override
    public Product findById(Integer id) {
        return productDao.findById(id).orElse(null);
    }

    @Override
    public Integer creatProduct(ProductRequest productRequest) {
        Product product = requestToObj(null,productRequest);
        productDao.save(product);
        return  productDao.save(product).getProductId();

    }

    @Override
    public Product updateProduct(Integer productId,ProductRequest productRequest) {
        Product product = productDao.findById(productId).orElse(null);

        if (product != null) {
            requestToObj(product,productRequest);
            productDao.save(product);
        }
        return product;
    }

    @Override
    public void deleteProduct(Integer productId) {
        productDao.deleteById(productId);
    }

    public Product requestToObj(Product product, ProductRequest productRequest){
        Date date = new Date();
        if (product == null){
            product = new Product();
            product.setCreatedDate(date);
        }
        product.setProductName(productRequest.getProductName());
        product.setCategory(productRequest.getCategory());
        product.setPrice(productRequest.getPrice());
        product.setStock(productRequest.getStock());
        product.setImageUrl(productRequest.getImageUrl());
        product.setDescription(productRequest.getDescription());
        product.setLastModifiedDate(date);
        return product;
    }
}
