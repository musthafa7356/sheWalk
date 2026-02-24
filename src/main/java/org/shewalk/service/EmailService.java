package org.shewalk.service;


import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendTrackingLink(String toEmail, String link) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Emergency Alert - SheWalk");
        message.setText(
                "Emergency Alert!\n\n" +
                        "Live tracking:\n" +
                        link
        );

        mailSender.send(message);
    }
}
