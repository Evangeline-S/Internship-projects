package com.softwiz.searchMs.Controller;

import com.softwiz.searchMs.entity.Product;
import com.softwiz.searchMs.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
public class SearchController {
    @Autowired
    private static ProductService productService;

    public SearchController() {
    }

    @PostMapping({"/create"})
    public static ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product createdProduct =productService.createProduct(product);
        return new ResponseEntity(createdProduct, HttpStatus.CREATED);
    }

    @GetMapping({"/search"})
    public static ResponseEntity<?> searchProducts(@RequestParam(required = true) String keyword) {
        if (keyword.isBlank()) {
            return ResponseEntity.badRequest().body("Keyword parameter cannot be empty");
        } else {
            try {
                List<Product> matchingProducts =productService.searchProducts(keyword);
                return matchingProducts.isEmpty() ? ResponseEntity.ok("No products found for the provided keyword: " + keyword) : ResponseEntity.ok(matchingProducts);
            } catch (Exception var3) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
            }
        }
    }
}

