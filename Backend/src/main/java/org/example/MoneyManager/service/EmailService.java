package org.example.MoneyManager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class EmailService {
    private final JavaMailSender mailSender;
    private final BrevoEmailClient brevoEmailClient;

    @Value("${email.smtp.enabled:true}")
    private boolean smtpEnabled;

    @Value("${spring.mail.properties.mail.smtp.from}")
    private String fromEmail;

    @Value("${spring.mail.username:}")
    private String fallbackFromEmail;

    public void sendEmail(String to,String subject,String body){
        if (smtpEnabled) {
            try {
                String resolvedFrom = StringUtils.hasText(fromEmail) ? fromEmail : fallbackFromEmail;
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom(resolvedFrom);
                message.setTo(to);
                message.setSubject(subject);
                message.setText(body);
                mailSender.send(message);
                return;
            } catch (Exception e) {
                // swallow and fall back to API
            }
        }
        // API fallback or primary path when SMTP disabled
        try {
            brevoEmailClient.sendTransactionalEmail(to, subject, body);
        } catch (Exception apiEx) {
            throw new RuntimeException(apiEx.getMessage());
        }
    }
}
