package com.eemarisademello.auth_service.controller;

import com.eemarisademello.auth_service.dto.GoogleTokenDTO;
import com.eemarisademello.auth_service.security.JwtUtil;
import com.eemarisademello.auth_service.service.GoogleTokenVerifierService;
import com.eemarisademello.auth_service.service.UserService;
import com.eemarisademello.eletiva_geotec_client.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final GoogleTokenVerifierService googleTokenVerifierService;

    //Simulação de autenticação simples
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
//        String username = credentials.get("username");
//        String password = credentials.get("password");
//
//        //Autenticação simulada
//        if ("admin".equals(username) && "senha123".equals(password)) {
//            String token = jwtUtil.generateToken(username);
//            return ResponseEntity.ok(Map.of("token", token));
//        } else {
//            return ResponseEntity.status(401).body("Credenciais inválidas");
//        }
//    }

    @PostMapping("/Oauth/google/login")
    public ResponseEntity<?> googleOauthLogin(@RequestBody GoogleTokenDTO credentials) {
        String googleToken = credentials.getToken();
        System.out.println("Requisição recebida com token: " + googleToken);
        System.out.println("Cliente ID do Google: " + googleTokenVerifierService.getClientId());

        try {
            var payload = googleTokenVerifierService.verifyToken(googleToken);
            String email = payload.getEmail();

            System.out.println("Payload do token verificado: " + payload);
            System.out.println("Email extraído do token: " + email);

            // Check if user exists in users service
            var user = userService.getUserByEmail(email);
            if (user == null) {
                return ResponseEntity.status(401).body("Usuário não autorizado");
            }

            // If user exists generate JWT token
            String jwt = jwtUtil.generateToken(email);
            return ResponseEntity.ok(Map.of("token", jwt, "user", user));

        } catch (Exception e) {
            return ResponseEntity.status(401).body("Erro na autenticação com o Google: " + e.getMessage());
        }
    }
}

