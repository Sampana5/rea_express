package com.rea.express.oauth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

/**
 * Échange le code d'autorisation GitHub contre un access token,
 * puis récupère le profil (nom + e-mail vérifié) de l'utilisateur.
 */
@Slf4j
@Component
public class GithubOAuthClient {

    private static final String TOKEN_URL = "https://github.com/login/oauth/access_token";
    private static final String USER_URL = "https://api.github.com/user";
    private static final String EMAILS_URL = "https://api.github.com/user/emails";

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${oauth.github.client-id:}")
    private String clientId;

    @Value("${oauth.github.client-secret:}")
    private String clientSecret;

    public GithubProfile fetchProfile(String code) {
        if (clientId == null || clientId.isBlank() || clientSecret == null || clientSecret.isBlank()) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "La connexion GitHub n'est pas encore configurée sur le serveur.");
        }

        String accessToken = exchangeCode(code);
        Map<String, Object> user = getJson(USER_URL, accessToken);
        if (user == null) {
            throw new BadCredentialsException("Impossible de récupérer votre profil GitHub.");
        }

        String email = (String) user.get("email");
        if (email == null || email.isBlank()) {
            email = fetchPrimaryEmail(accessToken);
        }
        if (email == null || email.isBlank()) {
            throw new BadCredentialsException(
                    "Aucune adresse e-mail vérifiée n'est associée à votre compte GitHub.");
        }

        String name = user.get("name") != null ? String.valueOf(user.get("name"))
                : String.valueOf(user.getOrDefault("login", email));
        return new GithubProfile(email, name);
    }

    private String exchangeCode(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        Map<String, String> body = Map.of(
                "client_id", clientId,
                "client_secret", clientSecret,
                "code", code
        );

        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.postForObject(
                    TOKEN_URL, new HttpEntity<>(body, headers), Map.class);
            Object token = response != null ? response.get("access_token") : null;
            if (token == null) {
                log.warn("Échange de code GitHub sans access_token : {}", response);
                throw new BadCredentialsException("Code GitHub invalide ou expiré. Réessayez.");
            }
            return String.valueOf(token);
        } catch (RestClientException ex) {
            log.error("Échec de l'échange du code GitHub", ex);
            throw new BadCredentialsException("Connexion GitHub impossible pour le moment.");
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getJson(String url, String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.set("Accept", "application/vnd.github+json");
        return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), Map.class).getBody();
    }

    @SuppressWarnings("unchecked")
    private String fetchPrimaryEmail(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.set("Accept", "application/vnd.github+json");
        try {
            List<Map<String, Object>> emails = restTemplate.exchange(
                    EMAILS_URL, HttpMethod.GET, new HttpEntity<>(headers), List.class).getBody();
            if (emails == null) {
                return null;
            }
            return emails.stream()
                    .filter(e -> Boolean.TRUE.equals(e.get("verified")))
                    .sorted((a, b) -> Boolean.compare(
                            Boolean.TRUE.equals(b.get("primary")),
                            Boolean.TRUE.equals(a.get("primary"))))
                    .map(e -> String.valueOf(e.get("email")))
                    .findFirst()
                    .orElse(null);
        } catch (RestClientException ex) {
            log.warn("Impossible de lister les e-mails GitHub", ex);
            return null;
        }
    }

    public record GithubProfile(String email, String name) {
    }
}
