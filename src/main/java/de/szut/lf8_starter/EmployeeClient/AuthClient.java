package de.szut.lf8_starter.EmployeeClient;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Component
public class AuthClient {

    private static final String TOKEN_URL =
            "http://keycloak.szut.dev/auth/realms/szut/protocol/openid-connect/token";

    private final RestTemplate restTemplate = new RestTemplate();

    public String fetchAccessToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String body = "grant_type=password&client_id=employee-management-service" +
                "&username=user&password=test";

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                TOKEN_URL, HttpMethod.POST, request, Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return (String) response.getBody().get("access_token");
        }

        throw new RuntimeException("Konnte kein Access Token von Keycloak abrufen");
    }
}
