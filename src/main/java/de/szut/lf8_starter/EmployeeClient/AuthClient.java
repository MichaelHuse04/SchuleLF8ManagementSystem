package de.szut.lf8_starter.EmployeeClient;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Map;

@Component
public class AuthClient {

    private static final String TOKEN_URL =
            "http://keycloak.szut.dev/auth/realms/szut/protocol/openid-connect/token";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public AuthClient() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(5000);
        this.restTemplate = new RestTemplate(factory);
        this.objectMapper = new ObjectMapper();
    }

    public String fetchAccessToken() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
            form.add("grant_type", "password");
            form.add("client_id", "employee-management-service");
            form.add("username", "user");
            form.add("password", "test");

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(form, headers);

            // Versuche zuerst den ursprünglichen Endpoint
            ResponseEntity<String> response = restTemplate.exchange(
                    TOKEN_URL,
                    HttpMethod.POST,
                    request,
                    String.class
            );

            // Falls Redirect, folge ihm manuell
            int maxRedirects = 5;
            int redirectCount = 0;

            while (response.getStatusCode().is3xxRedirection() && redirectCount < maxRedirects) {
                URI location = response.getHeaders().getLocation();
                if (location == null) {
                    throw new RuntimeException("Redirect ohne Location-Header");
                }

                response = restTemplate.exchange(
                        location,
                        HttpMethod.POST,
                        request,
                        String.class
                );
                redirectCount++;
            }

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                String body = response.getBody().trim();
                Map<String, Object> tokenResponse = objectMapper.readValue(body, new TypeReference<Map<String, Object>>() {
                });

                Object accessToken = tokenResponse.get("access_token");
                if (accessToken != null) {
                    return accessToken.toString();
                }
                throw new RuntimeException("access_token nicht im Response gefunden");
            }

            throw new RuntimeException("Token-Anfrage fehlgeschlagen. Status: " + response.getStatusCode());

        } catch (Exception e) {
            throw new RuntimeException("Fehler beim Abrufen des Access Tokens: " + e.getMessage(), e);
        }
    }
}