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

        try {

            System.out.println("Trusted email: " + toEmail);
            System.out.println("Tracking link: " + link);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Emergency Alert - SheWalk");
            message.setText("Live tracking:\n" + link);

            mailSender.send(message);
            System.out.println("EMAIL SENT SUCCESSFULLY");
            System.out.println("AFTER SEND");

        } catch (Exception e) {
            System.err.println("!!! EMAIL ERROR !!!: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
