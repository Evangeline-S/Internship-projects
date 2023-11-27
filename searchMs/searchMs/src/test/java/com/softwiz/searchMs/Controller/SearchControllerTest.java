package com.softwiz.searchMs.Controller;

import com.softwiz.searchMs.entity.Product;
import com.softwiz.searchMs.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class SearchControllerTest {
    @Mock
    private ProductService productService;

    @InjectMocks
    private SearchController searchController;


    public SearchControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateProduct() {
        // Mocking a product to be created
        Product productToCreate = new Product();
        productToCreate.setName("Test Product");
        productToCreate.setCategory("Test Category");

        // Mocking the creation of product in the service
        when(productService.createProduct(any(Product.class))).thenReturn(productToCreate);

        // Performing the POST request
        ResponseEntity<Product> response = searchController.createProduct(productToCreate);

        // Assertions
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Test Product", response.getBody().getName());
        assertEquals("Test Category", response.getBody().getCategory());
    }

    @Test
    public void testSearchProducts_WithValidKeyword() {
        // Mocking the ProductService behavior for a valid keyword
        List<Product> matchingProducts = new ArrayList<>();
        matchingProducts.add(new Product(1L, "Product1", "Category1"));
        matchingProducts.add(new Product(2L, "Product2", "Category2"));

        // Mocking repository behavior
        when(productService.searchProducts(anyString())).thenReturn(matchingProducts);

        // Performing the search with a valid keyword
        ResponseEntity<?> response = searchController.searchProducts("test");

        // Assertions for a valid search result
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof List);
        assertEquals(2, ((List<?>) response.getBody()).size());
    }
    @Test
    public void testSearchProducts_NoProductsFound() {
        // Mocking ProductService behavior for an empty result
        when(productService.searchProducts(anyString())).thenReturn(new ArrayList<>());

        // Performing the search with a keyword that yields no results
        ResponseEntity<?> response = searchController.searchProducts("nonexistent");

        // Assertions for no products found
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("No products found for the provided keyword: nonexistent", response.getBody());
    }

    @Test
    public void testSearchProducts_ExceptionHandling() {
        // Mocking ProductService behavior to throw an exception
        when(productService.searchProducts(anyString())).thenThrow(new RuntimeException("Something went wrong"));

        // Performing the search with a keyword that causes an exception
        ResponseEntity<?> response = searchController.searchProducts("error");

        // Assertions for exception handling
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred", response.getBody());
    }

    @Test
    public void testSearchProducts_WithEmptyKeyword() {
        // Performing the search with an empty keyword
        ResponseEntity<?> response = searchController.searchProducts("");

        // Assertions
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Keyword parameter cannot be empty", response.getBody());
    }
}