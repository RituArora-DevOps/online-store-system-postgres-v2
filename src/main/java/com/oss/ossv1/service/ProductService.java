package com.oss.ossv1.service;

import com.oss.ossv1.entity.Customer;
import com.oss.ossv1.data.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public Iterable<Customer> listProducts() {
        return productRepository.findAll();
    }

    public Customer getProduct(Long id) {
        return productRepository.findById(id).get();
    }

    public Customer createProduct(Customer product) {
        return productRepository.save(product);
    }

    public Customer updateProduct(Customer product) {
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public void deleteAllProducts() {
        productRepository.deleteAll();
    }
}
