package org.example.MoneyManager.controller;

import lombok.RequiredArgsConstructor;
import org.example.MoneyManager.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class EmailTestController {

    private final EmailService emailService;

    @Value("${email.smtp.enabled:true}")
    private boolean smtpEnabled;

    @Value("${brevo.api.key:}")
    private String brevoApiKey;

    @Value("${brevo.sender:}")
    private String brevoSender;

    @GetMapping("/email-config")
    public ResponseEntity<?> getEmailConfig() {
        return ResponseEntity.ok().body(new EmailConfigResponse(
            smtpEnabled,
            brevoApiKey != null && !brevoApiKey.isBlank(),
            brevoSender != null && !brevoSender.isBlank()
        ));
    }

    @PostMapping("/send-test-email")
    public ResponseEntity<?> sendTestEmail(@RequestParam String to) {
        try {
            emailService.sendEmail(to, "Test Email", "This is a test email from MoneyMate backend.");
            return ResponseEntity.ok().body("Test email sent successfully to: " + to);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to send test email: " + e.getMessage());
        }
    }

    public record EmailConfigResponse(
        boolean smtpEnabled,
        boolean brevoApiKeyConfigured,
        boolean brevoSenderConfigured
    ) {}
}
