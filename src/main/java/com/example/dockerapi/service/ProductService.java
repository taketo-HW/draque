package com.example.dockerapi.service;

import com.example.dockerapi.entity.Product;
import com.example.dockerapi.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository repo;

    public ProductService(ProductRepository repo) {
        this.repo = repo;
    }

    public Product createProduct(Product product) {
        return repo.save(product);
    }

    public Optional<Product> getProductById(Long id) {
        return repo.findById(id);
    }

    public Product updateProduct(Product product) {
        return repo.save(product);
    }

    public void deleteProduct(Long id) {
        repo.deleteById(id);
    }
}
