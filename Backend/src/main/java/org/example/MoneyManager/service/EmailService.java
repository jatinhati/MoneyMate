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
            throw new RuntimeException(e.getMessage());
        }
    }
}
