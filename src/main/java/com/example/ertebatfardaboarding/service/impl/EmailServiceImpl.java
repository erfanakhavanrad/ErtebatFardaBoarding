package com.example.ertebatfardaboarding.service.impl;

import com.example.ertebatfardaboarding.service.EmailService;
import jakarta.mail.*;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Properties;


@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

//    @Value("${spring.mail.host}")
//    String mailServer;
//
//    @Value("${spring.mail.username}")
//    String username;
//
//    @Value("${spring.mail.password}")
//    String mailServerPassword;
//
//    @Value("${spring.mail.port}")
//    String port;
//
//    @Value("${sender_email}")
//    String from;

//    @Autowired
//    private JavaMailSender javaMailSender;
//
//    public void sendSimpleEmail(String toEmail, String subject, String body) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(toEmail);
//        message.setSubject(subject);
//        message.setText(body);
//        message.setFrom(from);
//        javaMailSender.send(message);
//    }
//
//    public void sendHtmlEmail(String toEmail, String subject, String htmlBody) throws MessagingException {
//        MimeMessage message = javaMailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(message, true);
//
//        helper.setTo(toEmail);
//        helper.setSubject(subject);
//        helper.setText(htmlBody, true);
//        helper.setFrom(from);
//    }

    String to = "wiweseh516@lisoren.com";
    String from = "mailtrap@demomailtrap.com";
    String username = "api";
    String password = "f8538f1f18fa9011e053e7488836e721";

    String host = "live.smtp.mailtrap.io";


    public void sendSimpleEmail(String toEmail, String subject, String body) {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username,password);
            }
        });

        try{
            Message message= new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipient(Message.RecipientType.TO,new InternetAddress(to));
            message.setSubject("My subject");
            message.setText("MY BODY");
            Transport.send(message);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
