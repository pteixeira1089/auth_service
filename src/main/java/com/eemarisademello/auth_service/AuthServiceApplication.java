package com.eemarisademello.auth_service;

import com.eemarisademello.auth_service.security.JwtUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthServiceApplication.class, args);

		System.out.println("Chave gerada (uso único!) em coficaçãa Base64: "
		 + JwtUtil.generateBase64SecretKey());
	}
}
