package com.api.market.service;

import com.api.market.model.Category;
import com.api.market.repository.CategoryRepository;
import com.api.market.request.CategoryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

@Service
public class CategoryService {
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    public Category create(CategoryRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Ya existe una categoría con ese nombre");
        }
        
        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setActive(request.isActive());
        
        return categoryRepository.save(category);
    }
    
    public Category getById(Long id) {
        return categoryRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada"));
    }
    
    public Category update(Long id, CategoryRequest request) {
        Category category = getById(id);
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setActive(request.isActive());
        
        return categoryRepository.save(category);
    }
    
    public void delete(Long id) {
        Category category = getById(id);
        categoryRepository.delete(category);
    }

    // Métodos públicos
    public List<Category> getAllActiveCategories() {
        return categoryRepository.findByActiveTrue();
    }
}
