package com.api.market.resource;

import com.api.market.model.Category;
import com.api.market.model.Product;
import com.api.market.service.CategoryService;
import com.api.market.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controlador para endpoints públicos
 * No requiere autenticación JWT
 */
@RestController
@RequestMapping("/api/public")
public class PublicController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    /**
     * Lista todos los productos activos y con stock
     */
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAvailableProducts() {
        return ResponseEntity.ok(productService.getAllAvailableProducts());
    }

    /**
     * Lista todas las categorías activas
     */
    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getActiveCategories() {
        return ResponseEntity.ok(categoryService.getAllActiveCategories());
    }
}
