package com.eemarisademello.auth_service.service;

import com.eemarisademello.auth_service.dto.GoogleUserInfoDTO;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class GoogleTokenVerifierService {
    private final GoogleIdTokenVerifier verifier;
    private final String clientId;

    public GoogleTokenVerifierService(@Value("${google.oauth.client-id}") String clientId) {
        this.clientId = clientId;

        // DEBUG: imprima o que veio do Spring e do sistema
        System.out.println(">>> [DEBUG] clientId injetado pelo Spring: " + this.clientId);
        System.out.println(">>> [DEBUG] System.getenv(\"GOOGLE_OAUTH_CLIENT_ID\"): "
                + System.getenv("GOOGLE_OAUTH_CLIENT_ID"));


        this.verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(),
                JacksonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(clientId))
                .build();


        /* This implementation follow Google's official documentation
        // In this, transport and jsonFactory are not defined in the snippet.
        this.verifier = new GoogleIdTokenVerifier(transport, jsonFactory)
                .setAudience(Collections.singletonList(clientId))
                .build();

         */
    }

    public GoogleUserInfoDTO verifyToken(String token) {
        try {
            GoogleIdToken idToken = verifier.verify(token);
            System.out.println("Verificando token Google: " + token);
            //System.out.println("Payload do idToken: " + idToken.getPayload());
            if (idToken != null) {
                Payload payload = idToken.getPayload();

                //Print user information for debugging
                System.out.println(payload);

                String userName = payload.getSubject();
                String userEmail = payload.getEmail();

                System.out.println("User name: " + userName);
                System.out.println("Email: " + userEmail);


                return new GoogleUserInfoDTO(userName, userEmail);
            } else {
                System.out.println("Token inválido ou expirado");
                throw new RuntimeException("Token inválido");
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao verificar token Google: " + e.getMessage());
        }
    }

    public String getClientId() {
        return clientId;
    }
}
