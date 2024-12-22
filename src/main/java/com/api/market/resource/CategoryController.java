package com.api.market.resource;

import com.api.market.model.Category;
import com.api.market.request.CategoryRequest;
import com.api.market.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    
    @Autowired
    private CategoryService categoryService;
    
    @PostMapping
    public ResponseEntity<Category> create(@Valid @RequestBody CategoryRequest request) {
        Category category = categoryService.create(request);
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Category> getById(@PathVariable Long id) {
        Category category = categoryService.getById(id);
        return ResponseEntity.ok(category);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Category> update(@PathVariable Long id, @Valid @RequestBody CategoryRequest request) {
        Category category = categoryService.update(id, request);
        return ResponseEntity.ok(category);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
