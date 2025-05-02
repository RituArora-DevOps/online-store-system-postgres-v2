package com.oss.ossv1.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oss.ossv1.data.entity.Product;

public class ProductRepository extends JpaRepository<Product, Long> {
}
