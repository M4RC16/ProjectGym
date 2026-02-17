package com.gymprojekt.forevergym.service;

import com.gymprojekt.forevergym.controller.ContactFormController;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    String phoneRegex = "^[0-9+ ]{7,15}$";

    private final JavaMailSender mailSender;
    private final UserService userService;

    public EmailService(JavaMailSender mailSender, UserService userService) {
        this.mailSender = mailSender;
        this.userService = userService;
    }

    public void SendVerificationEmail(String toEmail, String token) {
        String verificationLink = "http://localhost:8080/verify/" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Fiók aktiválás - ForeverGym");
        message.setText("15 perce van az aktiválásra\n" +
                "Az aktiváláshoz kattintson ide: " + verificationLink);

        mailSender.send(message);
    }

    public void SendPasswordResetEmail(String toEmail) {

        String token = userService.sendToken(toEmail);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Jelszó megváltoztatás - ForeverGym");
        message.setText("Írd be a kódot" +
                "a kód: " + token);

        mailSender.send(message);

    }

    public void SendDeleteReservation(String toEmail) {

        String token = userService.sendToken(toEmail);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Jelszó megváltoztatás - ForeverGym");
        message.setText("Írd be a kódot" +
                "a kód: " + token);

        mailSender.send(message);

    }

    public String SendForm(ContactFormController.FormInfo form) {
        if (form.getPhoneNumber() != null && !form.getPhoneNumber().matches(phoneRegex)) {
            throw new RuntimeException("Érvénytelen telefonszám formátum!");
        }
        if (form.getEmailAddress() != null && !form.getEmailAddress().matches(emailRegex)) {
            throw new RuntimeException("Érvénytelen email formátum");
        }   
        if (form.getMessage() == null) {
            throw new RuntimeException("Nem lehet üres az üzenet mező");
        }
        if (form.getFirstName() == null || form.getLastName() == null) {
            throw new RuntimeException("Nem lehet üres a név");
        }
        SimpleMailMessage message = MailMessage(form);

        mailSender.send(message);

        return "Sikeres Küldés";
    }

    private static SimpleMailMessage MailMessage(ContactFormController.FormInfo form) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("schzsombor@gmail.com");
        message.setSubject("ÚJ KAPCSOLATFELVÉTEL: " + form.getFirstName() + " " + form.getLastName());

        String emailBody = String.format(
                """
                                Új üzenet érkezett a weboldalról:
                        
                                Név: %s %s
                                Email: %s
                                Telefonszám: %s
                        
                                Üzenet:
                                %s
                        """,
                form.getFirstName(), form.getLastName(), form.getEmailAddress(), form.getPhoneNumber(), form.getMessage()
        );

        message.setText(emailBody);
        return message;
    }
}
