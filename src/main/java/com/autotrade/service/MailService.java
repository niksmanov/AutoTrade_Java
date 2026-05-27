package com.autotrade.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class MailService {
    private final JavaMailSender mailSender;
    private final String from;
    private final String username;

    public MailService(JavaMailSender mailSender,
                       @Value("${autotrade.mail.from}") String from,
                       @Value("${spring.mail.username:}") String username) {
        this.mailSender = mailSender;
        this.from = from;
        this.username = username;
    }

    public void send(String to, String subject, String body) {
        if (!StringUtils.hasText(username)) {
            return;
        }

        try {
            var message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
        } catch (MailException ignored) {
            // Email delivery should not break the JSON flow.
        }
    }
}
