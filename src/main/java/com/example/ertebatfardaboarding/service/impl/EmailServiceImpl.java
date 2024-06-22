package com.example.ertebatfardaboarding.service.impl;

import com.example.ertebatfardaboarding.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


@Service
@Slf4j
public class EmailServiceImpl implements EmailService {
    private static final String SMTP_HOST_NAME = "smtp.mail.yahoo.com";
    private static final String SMTP_AUTH_USER = "erfanakhavanrad@yahoo.com";
    private static final String SMTP_AUTH_PWD = "";

    public void sendSimpleEmail(String toEmail, String subject, String body2) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST_NAME);
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.ssl", "true");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(SMTP_AUTH_USER, SMTP_AUTH_PWD);
                    }
                });

        Transport transport = session.getTransport("smtp");

        MimeMessage message = new MimeMessage(session);
        message.setContent("This is a test", "text/plain");
        message.setFrom(new InternetAddress("erfanakhavanrad@yahoo.com"));
        message.addRecipient(Message.RecipientType.TO,
                new InternetAddress("erfanakhavanrad@yahoo.com"));

        transport.connect();
        transport.sendMessage(message,
                message.getRecipients(Message.RecipientType.TO));
        transport.close();
    }
}

