package com.softwiz.searchMs.service;

import com.softwiz.searchMs.entity.Product;
import com.softwiz.searchMs.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(Product product) {
        return (Product)this.productRepository.save(product);
    }

    public List<Product> searchProducts(String keyword) {
        String processedKeyword = this.preprocessKeyword(keyword);
        List<Product> allProducts = this.productRepository.findAll();
        return (List)allProducts.stream().filter((product) -> {
            return this.preprocessText(product.getName()).contains(processedKeyword) || this.preprocessText(product.getDescription()).contains(processedKeyword);
        }).collect(Collectors.toList());
    }

    private String preprocessKeyword(String keyword) {
        String processedKeyword = keyword.replaceAll("\\s", "").toLowerCase();
        if (processedKeyword.endsWith("s")) {
            processedKeyword = processedKeyword.substring(0, processedKeyword.length() - 1);
        }

        return processedKeyword;
    }

    private String preprocessText(String text) {
        return text.replaceAll("\\s", "").toLowerCase();
    }
}

