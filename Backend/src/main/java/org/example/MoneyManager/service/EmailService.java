package org.example.MoneyManager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class EmailService {
    private final JavaMailSender mailSender;
    private final BrevoEmailClient brevoEmailClient;

    @Value("${spring.mail.properties.mail.smtp.from}")
    private String fromEmail;

    @Value("${spring.mail.username:}")
    private String fallbackFromEmail;

    public void sendEmail(String to,String subject,String body){
        try {
            String resolvedFrom = StringUtils.hasText(fromEmail) ? fromEmail : fallbackFromEmail;
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(resolvedFrom);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
        }catch (Exception e){
            // Fallback to Brevo HTTP API if SMTP is blocked
            try {
                brevoEmailClient.sendTransactionalEmail(to, subject, body);
            } catch (Exception apiEx) {
                throw new RuntimeException(apiEx.getMessage());
            }
        }
    }
}
