package com.rea.express.oauth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

/**
 * Vérifie un ID token émis par Google Identity Services via l'endpoint
 * officiel tokeninfo, puis en extrait le profil (e-mail vérifié + nom).
 */
@Slf4j
@Component
public class GoogleTokenVerifier {

    private static final String TOKEN_INFO_URL = "https://oauth2.googleapis.com/tokeninfo?id_token={token}";

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${oauth.google.client-id:}")
    private String clientId;

    public GoogleProfile verify(String idToken) {
        if (clientId == null || clientId.isBlank()) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "La connexion Google n'est pas encore configurée sur le serveur.");
        }

        Map<String, Object> claims;
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.getForObject(TOKEN_INFO_URL, Map.class, idToken);
            claims = response;
        } catch (HttpClientErrorException ex) {
            log.warn("Jeton Google rejeté par tokeninfo : {}", ex.getStatusCode());
            throw new BadCredentialsException("Jeton Google invalide ou expiré.");
        }

        if (claims == null || !clientId.equals(claims.get("aud"))) {
            throw new BadCredentialsException("Jeton Google invalide (application non reconnue).");
        }
        if (!"true".equals(String.valueOf(claims.get("email_verified")))) {
            throw new BadCredentialsException("Votre adresse Google n'est pas vérifiée.");
        }

        String email = (String) claims.get("email");
        if (email == null || email.isBlank()) {
            throw new BadCredentialsException("Impossible de récupérer votre adresse e-mail Google.");
        }
        String name = (String) claims.getOrDefault("name", email);
        return new GoogleProfile(email, name);
    }

    public record GoogleProfile(String email, String name) {
    }
}
