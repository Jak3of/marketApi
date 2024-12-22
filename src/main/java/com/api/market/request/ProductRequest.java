package com.api.market.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductRequest {
    @NotBlank(message = "El nombre es obligatorio")
    private String name;
    
    private String description;
    
    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor a 0")
    private BigDecimal price;
    
    @NotNull(message = "El stock es obligatorio")
    @Positive(message = "El stock debe ser mayor a 0")
    private Integer stock;
    
    private boolean active = true;
    
    @NotNull(message = "La categoría es obligatoria")
    private Long categoryId;
}
