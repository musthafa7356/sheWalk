package org.shewalk.service;

import sendinblue.ApiClient;
import sendinblue.Configuration;
import sendinblue.auth.ApiKeyAuth;
import sibModel.*;
import sibApi.TransactionalEmailsApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
public class EmailService {

    @Value("${BREVO_API_KEY}")
    private String apiKey;

    @Value("${SENDER_EMAIL}")
    private String senderEmail;

    @Async
    public void sendTrackingLink(String toEmail, String link) {
        try {
            System.out.println("Attempting to send API email to: " + toEmail);

            // Configure API Client
            ApiClient defaultClient = Configuration.getDefaultApiClient();
            ApiKeyAuth apiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("api-key");
            apiKeyAuth.setApiKey(apiKey);


            TransactionalEmailsApi apiInstance = new TransactionalEmailsApi();

            // Create the Email Object
            SendSmtpEmail sendSmtpEmail = new SendSmtpEmail();

            // Sender details (Dynamic from Render Environment)
            sendSmtpEmail.setSender(new SendSmtpEmailSender().email(senderEmail).name("SheWalk Emergency"));

            // Receiver details
            sendSmtpEmail.setTo(Collections.singletonList(new SendSmtpEmailTo().email(toEmail)));

            // Subject and Content
            sendSmtpEmail.setSubject("🚨 Emergency Alert - SheWalk 🚨");
            sendSmtpEmail.setHtmlContent(
                    "<h2>Emergency Alert!</h2>" +
                            "<p>A user has triggered an SOS. You are their trusted contact.</p>" +
                            "<p><b>Live Tracking Link:</b> <a href='" + link + "'>" + link + "</a></p>" +
                            "<p>Please check on them immediately.</p>"
            );

            // Send via HTTP (This works on Render!)
            apiInstance.sendTransacEmail(sendSmtpEmail);
            System.out.println("EMAIL SENT VIA API SUCCESSFULLY");

        } catch (Exception e) {
            System.err.println("!!! BREVO API ERROR !!!: " + e.getMessage());
            e.printStackTrace();
        }
    }
}