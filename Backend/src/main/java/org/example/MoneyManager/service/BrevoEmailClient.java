package org.example.MoneyManager.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class BrevoEmailClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${brevo.api.key:}")
    private String brevoApiKey;

    @Value("${brevo.sender:}")
    private String defaultSender;

    public void sendTransactionalEmail(String to, String subject, String textContent) {
        if (brevoApiKey != null) {
            brevoApiKey = brevoApiKey.trim();
        }
        if (brevoApiKey == null || brevoApiKey.isBlank()) {
            throw new IllegalStateException("Brevo API key not configured");
        }
        if (!brevoApiKey.startsWith("xkeysib-")) {
            throw new IllegalStateException("Brevo API key appears invalid (expected to start with xkeysib-)");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.add("api-key", brevoApiKey);

        Map<String, Object> body = new HashMap<>();
        Map<String, String> sender = new HashMap<>();
        sender.put("email", defaultSender);
        body.put("sender", sender);
        body.put("to", List.of(Map.of("email", to)));
        body.put("subject", subject);
        body.put("textContent", textContent);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    "https://api.brevo.com/v3/smtp/email",
                    entity,
                    String.class
            );
            if (!response.getStatusCode().is2xxSuccessful()) {
                String responseBody = response.getBody();
                throw new RuntimeException("Brevo API error: " + response.getStatusCode() + (responseBody != null ? (" - " + responseBody) : ""));
            }
        } catch (HttpStatusCodeException httpEx) {
            String responseBody = httpEx.getResponseBodyAsString();
            throw new RuntimeException("Brevo API error: " + httpEx.getStatusCode() + (responseBody != null ? (" - " + responseBody) : ""));
        }
    }
}


