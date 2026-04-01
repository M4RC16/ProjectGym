package com.projectgym.service;

import com.projectgym.model.*;
import com.projectgym.repository.*;
import com.projectgym.controller.UserController;
import com.projectgym.exception.NotValidException;
import com.projectgym.exception.NotFoundException;
import com.projectgym.exception.BadRequestException;
import com.projectgym.exception.AlreadyExistsException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.SecureRandom;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Value("${file.upload-dir-user}")
    private String uploadDir;

    String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    String passwordRegex = "^(?=.*[0-9])(?=.*[^a-zA-Z0-9\\s]).{8,}$";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final RoleRepository roleRepository;
    private final RefreshTokenRepository refreshRep;
    private final ReservationXUserRepository rxuRep;
    private final ReservationRepository resRep;
    private final TicketRepository ticketRep;
    @Lazy
    public UserService(TicketRepository ticketRep, ReservationRepository resRep, ReservationXUserRepository rxuRep, RefreshTokenRepository refreshRep, UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.roleRepository = roleRepository;
        this.refreshRep = refreshRep;
        this.rxuRep = rxuRep;
        this.resRep = resRep;
        this.ticketRep = ticketRep;
    }

    public void registerUser(User user) {

        Role newRole;

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new AlreadyExistsException("Az email regisztrálva van!");
        }
        if (!user.getEmail().matches(emailRegex)) {
            throw new NotValidException("Az emailcím amit beítrál nem felel meg!");
        }
        if (!user.getPassword().matches(passwordRegex)) {
            throw new BadRequestException("A jelszó legalább 8 karakter hosszú, legalább 1 számot és 1 speciális karaktert kell tartalmazzon!");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setVerified(false);
        String token = UUID.randomUUID().toString();
        user.setVerificationToken(token);
        user.setTokenExpiryDate(LocalDateTime.now().plusHours(12));
        newRole = roleRepository.findById(3).orElseThrow();
        user.setRole(newRole);

        emailService.SendVerificationEmail(user.getEmail(), token);
        userRepository.save(user);
    }

    public void verifyUser(String token) {
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new RuntimeException("A felhasználó nem található"));

        if (user.getTokenExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("A token lejárt, kérj új emailt");
        }

        user.setVerified(true);
        user.setVerificationToken(null);
        user.setTokenExpiryDate(null);
        userRepository.save(user);
    }

    private String generateAlphanumericCode() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(6);

        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }

        return sb.toString();
    }

    public String sendToken(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("A felhasználó nem található"));

        if (user.getResetTokenExpiryDate() != null && user.getResetTokenExpiryDate().isAfter(LocalDateTime.now())) {
            throw new RuntimeException("A kért token még érvényes, nem küldök újat.");
        }

        String resetToken = generateAlphanumericCode();
        user.setResetPasswordToken(resetToken);
        user.setResetTokenExpiryDate(LocalDateTime.now().plusMinutes(15));
        userRepository.save(user);

        return resetToken;
    }

    public void resetPassword(UserController.ForgottenPasswordRequest request) {

        String password;
        if (!Objects.equals(request.getPassword1(), request.getPassword2())) {
            throw new RuntimeException("A kettő beírt jelszó nem egyezik meg!");
        } else {
            password = request.getPassword1();
        }

        if (!password.matches(passwordRegex)) {
            throw new RuntimeException("A jelszó legalább 8 karakter hosszú, legalább 1 számot és 1 speciális karaktert kell tartalmazzon!");
        }

        User user = userRepository.findByResetPasswordToken(request.getToken())
                .orElseThrow(() -> new RuntimeException("A felhasználó nem található"));

        if (user.getResetTokenExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("A token lejárt, kérj új emailt");
        }

        user.setPassword(passwordEncoder.encode(password));
        user.setResetPasswordToken(null);
        user.setResetTokenExpiryDate(null);
        userRepository.save(user);
    }

    public List<UserProjection> getAllUsers() {
        return userRepository.findAllProjectedBy();
    }

    @Transactional
    public void changeUserRole(UserController.changeUserRole changeUser) {

        int userId = changeUser.getId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("A felhasználó nem található"));

        Role newRole = roleRepository.findById(changeUser.getRoleId())
                .orElseThrow(() -> new NotFoundException("Szerepkör nem található"));

        user.setRole(newRole);
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Nem található felhasználó"));
        int id = user.getId();
        refreshRep.deleteByUserId(id);
        List<ReservationXUser> reservations = rxuRep.findByUserId(id);
        reservations.forEach(reservation -> rxuRep.deleteById(Long.valueOf(reservation.getId())));
        reservations.forEach(reservation -> resRep.deleteById(reservation.getReservation().getId()));
        userRepository.deleteByEmail(email);
    }

    public List<UserController.Trainers> getAllTrainer() {

        List<TrainerProjection> projections = userRepository.findAllTrainers();

        return projections.stream()
                .map(projection -> new UserController.Trainers(
                        projection.getTrainerId(),
                        projection.getTrainerName(),
                        projection.getDescription(),
                        projection.getProfilePicture(),
                        projection.getHourlyWage()
                ))
                .collect(Collectors.toList());
    }

    public void deleteMe(String email) {
        userRepository.deleteByEmail(email);
    }

    public UserProjection getUserById(int id) {
        UserProjection user = userRepository.findUserById(id);
        if (user == null) {
            throw new NotFoundException("Felhasználó nem található ID: " + id);
        }

        return user;
    }

    public void changeName(String request, String firstName, String lastName) {

        User user = userRepository.findByEmail(request)
                .orElseThrow(() -> new NotFoundException("Felhasználó nem található ID: " + request));

        user.setFirstName(firstName);
        user.setLastName(lastName);
        userRepository.save(user);
    }

    public void changeNumber(String token, String request) {
        User user = userRepository.findByEmail(token)
                .orElseThrow(() -> new NotFoundException("Felhasználó nem található ID: " + token));

        user.setPhoneNumber(request);
        userRepository.save(user);
    }

    public String getpfp(String token) {
        User user = userRepository.findByEmail(token)
                .orElseThrow(() -> new NotFoundException("Felhasználó nem található ID: " + token));

        return user.getProfilePicture();
    }

    @Transactional
    public String changePfp(String email, MultipartFile pfp) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Felhasználó nem található:  " + email));

        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String originalName = pfp.getOriginalFilename();
            if (originalName == null || !originalName.contains(".")) {
                throw new BadRequestException("Érvénytelen fájlformátum!");
            }

            String newFileName = UUID.randomUUID() + originalName.substring(originalName.lastIndexOf("."));

            Path filePath = uploadPath.resolve(newFileName);
            Files.copy(pfp.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            String dbImagePath = "/var/www/projectgym/uploads/images/users/" + newFileName;
            user.setProfilePicture(dbImagePath);
            userRepository.save(user);

            return dbImagePath;
        } catch (Exception e) {
            throw new RuntimeException("Hiba a fájl létrehozásakor: " + e.getMessage());
        }
    }

    public String getEmailByRefreshToken(String refreshToken) {
        RefreshToken token = refreshRep.findByToken(refreshToken)
                .orElseThrow(() -> new NotFoundException("Refresh token nem található"));
        int id = token.getUser().getId();
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Felhasználó nem található ID: " + id));
        return user.getEmail();
    }

    public void changeDescription(String currentUserEmail, String description) {
        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new NotFoundException("Felhasználó nem található ID: " + currentUserEmail));
        user.setDescription(description);
        userRepository.save(user);
    }

    public void changeWage(String currentUserEmail, int wage) {
        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new NotFoundException("Felhasználó nem található ID: " + currentUserEmail));
        user.setHourlyWage(wage);
        userRepository.save(user);
    }

    public void addTicket(String currentUserEmail, int ticketId) {
        Ticket ticket = ticketRep.findTicketById(ticketId);
        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new NotFoundException("Felhasználó nem található ID: " + currentUserEmail));

        int length = ticket.getValidityLength();
        user.setHasValidTicket(true);
        user.setValidUntil(LocalDate.now().plusDays(length));
        userRepository.save(user);
    }
}