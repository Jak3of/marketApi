package com.api.market.repository;

import com.api.market.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name);  // Para verificar si ya existe una categoría con ese nombre es automatico
    List<Category> findByActiveTrue();  // Para listar categorías activas
}
