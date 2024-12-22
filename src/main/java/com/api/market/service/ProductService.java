package com.api.market.service;

import com.api.market.model.Product;
import com.api.market.model.Category;
import com.api.market.repository.ProductRepository;
import com.api.market.request.ProductRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private CategoryService categoryService;
    
    public Product create(ProductRequest request) {
        Category category = categoryService.getById(request.getCategoryId());
        
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setActive(request.isActive());
        product.setCategory(category);
        
        return productRepository.save(product);
    }
    
    public Product getById(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));
    }
    
    public Product update(Long id, ProductRequest request) {
        Product product = getById(id);
        Category category = categoryService.getById(request.getCategoryId());
        
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setActive(request.isActive());
        product.setCategory(category);
        
        return productRepository.save(product);
    }
    
    public void delete(Long id) {
        Product product = getById(id);
        productRepository.delete(product);
    }

    // Métodos públicos
    public List<Product> getAllActiveProducts() {
        return productRepository.findByActiveTrue();
    }

    public List<Product> getAllAvailableProducts() {
        return productRepository.findByActiveTrueAndStockGreaterThan(0);
    }
}
