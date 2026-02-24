package com.gymprojekt.projectgym.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final UserService userService;

    public EmailService(JavaMailSender mailSender, UserService userService) {
        this.mailSender = mailSender;
        this.userService = userService;
    }

    public void SendVerificationEmail(String toEmail, String token) {
        String verificationLink = "http://projectgym.hu/verify/" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setFrom("***REMOVED***");
        message.setSubject("Fiók aktiválás - ForeverGym");
        message.setText("15 perce van az aktiválásra\n" +
                "Az aktiváláshoz kattintson ide: " + verificationLink);

        mailSender.send(message);
    }

    public void SendPasswordResetEmail(String toEmail) {

        String token = userService.sendToken(toEmail);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setFrom("***REMOVED***");
        message.setSubject("Jelszó megváltoztatás - ForeverGym");
        message.setText("Írd be a kódot" +
                "a kód: " + token);

        mailSender.send(message);

    }

    public void SendDeleteReservation(String toEmail, LocalDateTime scheduledAt) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setFrom("***REMOVED***");
        message.setSubject("Lemondott óra - ForeverGym");
        message.setText("Egy ügyfél lemondta az órát.\n\n" +
                "Időpont: " + scheduledAt + "\n\n" +
                "Üdvözlettel,\nAutómata üzenetküldő");

        mailSender.send(message);

    }

    public void SendNewReservation(String toEmail, LocalDateTime scheduledAt) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Foglalt óra - ForeverGym");
        message.setFrom("***REMOVED***");
        message.setText("Egy ügyfél foglalt egy órát.\n\n" +
                "Időpont: " + scheduledAt + "\n\n" +
                "Üdvözlettel,\nAutómata üzenetküldő");

        mailSender.send(message);
    }
}
