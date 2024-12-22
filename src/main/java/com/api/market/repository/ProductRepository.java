package com.api.market.repository;

import com.api.market.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryId(Long categoryId);
    List<Product> findByActiveTrue();  // Para listar productos activos
    List<Product> findByActiveTrueAndStockGreaterThan(Integer stock);  // Para listar productos disponibles
}
