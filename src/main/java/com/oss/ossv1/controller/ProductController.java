package com.oss.ossv1.controller;

import com.oss.ossv1.data.entity.Product;
import com.oss.ossv1.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Product> getAllProducts() {
        return (List<Product>) productService.listProducts();
    }
}
