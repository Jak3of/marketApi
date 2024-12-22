package com.api.market.resource;

import com.api.market.bean.RegisterRequest;
import com.api.market.bean.TokenResponse;
import com.api.market.model.User;
import com.api.market.security.TokenUtils;
import com.api.market.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

/**
 * Controlador para manejar autenticación y registro
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final TokenUtils tokenUtils;

    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(@Valid @RequestBody RegisterRequest request) {
        // Registrar el usuario
        User user = userService.registerUser(request);
        
        // Extraer roles del usuario
        var roles = user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toList());
        
        // Generar token incluyendo roles
        String token = tokenUtils.createToken(user.getName(), user.getEmail(), roles);
        
        // Retornar el token
        return ResponseEntity.ok(new TokenResponse(token));
    }
}
