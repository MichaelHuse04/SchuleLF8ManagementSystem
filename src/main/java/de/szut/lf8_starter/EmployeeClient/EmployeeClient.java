package de.szut.lf8_starter.EmployeeClient;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class EmployeeClient {

    private final RestTemplate restTemplate;
    private final AuthClient authClient;
    private static final String BASE_URL = "https://employee.szut.dev/api/v1/employees";

    public EmployeeClient(AuthClient authClient) {
        this.restTemplate = new RestTemplate();
        this.authClient = authClient;
    }

    public boolean existsById(Long employeeId) {
        String token = authClient.fetchAccessToken();
        String url = BASE_URL + "/" + employeeId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        try {
            restTemplate.exchange(url, HttpMethod.GET, request, Object.class);
            return true;
        } catch (HttpClientErrorException.NotFound e) {
            return false;
        } catch (HttpClientErrorException.Unauthorized e) {
            throw new RuntimeException("Nicht autorisiert – Token ungültig oder abgelaufen.");
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Fehler beim Employee-Service: " + e.getStatusCode(), e);
        }
    }
}