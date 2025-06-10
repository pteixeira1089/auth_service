package com.eemarisademello.auth_service.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    private SecretKey signingKey;

    private static final long EXPIRATION_TIME_MS = 60 * 60 * 1000; // 1 hour

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @PostConstruct
    public void init() {
        signingKey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public static SecretKey generateSecretKey() {
        return Jwts.SIG.HS256.key().build();
    }

    public static String generateBase64SecretKey() {
        SecretKey key = generateSecretKey();
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }


    // Gerar token
    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuer("auth-service")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MS))
                .signWith(signingKey)
                .compact();
    }

    // Extrair informações
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Genérico para extrair qualquer claim
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = parseClaims(token);
        return claimsResolver.apply(claims);
    }

    // Parse e validação do token
    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

        // Validação
        public boolean isTokenExpired (String token){
            return extractExpiration(token).before(new Date());
        }

        public boolean isTokenValid (String token, String username){
            return extractUsername(token).equals(username) //valida usuário
                    && !isTokenExpired(token) //valida se o token expirou
                    && "auth.service".equals(parseClaims(token).getIssuer()); //valida emissor
        }
    }
