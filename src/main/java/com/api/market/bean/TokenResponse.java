package com.api.market.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Clase que representa la respuesta con el token JWT
 * Se usa para enviar el token al cliente después de un login exitoso
 * 
 * token: El token JWT generado que el cliente debe usar para futuras peticiones
 */
@Data
@AllArgsConstructor
public class TokenResponse {
    private String token;
}
