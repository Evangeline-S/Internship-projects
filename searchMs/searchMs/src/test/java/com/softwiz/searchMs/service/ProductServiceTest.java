package com.softwiz.searchMs.service;

import com.softwiz.searchMs.entity.Product;
import com.softwiz.searchMs.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateProduct() {
        Product product = new Product();
        product.setName("Test Product");

        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product createdProduct = productService.createProduct(product);

        assertEquals("Test Product", createdProduct.getName());
    }

    @Test
    public void testSearchProducts() {
        Product product1 = new Product();
        product1.setName("Product1");
        product1.setDescription("Description for Product1");

        Product product2 = new Product();
        product2.setName("Product2");
        product2.setDescription("Description for Product2");

        List<Product> productList = Arrays.asList(product1, product2);

        when(productRepository.findAll()).thenReturn(productList);

        List<Product> foundProducts = productService.searchProducts("Product");

        assertEquals(2, foundProducts.size());
    }

}