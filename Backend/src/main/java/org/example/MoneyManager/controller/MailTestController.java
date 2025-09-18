package org.example.MoneyManager.controller;

import lombok.RequiredArgsConstructor;
import org.example.MoneyManager.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal")
@RequiredArgsConstructor
public class MailTestController {

    private final EmailService emailService;

    @PostMapping("/test-mail")
    public ResponseEntity<String> sendTestMail(@RequestParam("to") String to) {
        try {
            emailService.sendEmail(to, "MoneyMate Test", "This is a test email from MoneyMate on Render.");
            return ResponseEntity.ok("Mail sent to: " + to);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed: " + e.getMessage());
        }
    }
}


