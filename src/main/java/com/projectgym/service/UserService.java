package com.projectgym.service;

import com.projectgym.repository.*;
import com.projectgym.controller.UserController;
import com.projectgym.exception.NotValidException;
import com.projectgym.exception.NotFoundException;
import com.projectgym.exception.BadRequestException;
import com.projectgym.exception.AlreadyExistsException;
import com.projectgym.model.Role;
import com.projectgym.model.User;
import com.projectgym.repository.*;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    String passwordRegex = "^(?=.*[0-9])(?=.*[!@#$%^&*]).{8,}$";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final RoleRepository roleRepository;
    private final RefreshTokenRepository refreshRep;

    @Lazy
    public UserService(RefreshTokenRepository refreshRep, UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.roleRepository = roleRepository;
        this.refreshRep = refreshRep;
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

        if (user.getResetTokenExpiryDate() != null && user.getResetTokenExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("A kért token még érvényes, nem küldök újat.");
        }

        String resetToken = generateAlphanumericCode();
        user.setResetPasswordToken(resetToken);
        user.setResetTokenExpiryDate(LocalDateTime.now().plusMinutes(15));
        userRepository.save(user);

        return resetToken;
    }

    public void resetPassword(UserController.PasswordResetRequest request) {

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
        userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Nem található felhasználó"));
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

    public void deleteMe(String email){
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
            Path uploadPath = Paths.get(uploadDir + pfp);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String originalName = pfp.getOriginalFilename();
            if (originalName == null || !originalName.contains(".")) {
                throw new BadRequestException("Érvénytelen fájlformátum!");
            }

            String newFileName = UUID.randomUUID() + originalName.substring(originalName.lastIndexOf(".") + 1);

            Path filePath = uploadPath.resolve(newFileName);
            Files.copy(pfp.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            String dbImagePath = "/images/" + newFileName;
            user.setProfilePicture(dbImagePath);
            userRepository.save(user);

            return dbImagePath;
        } catch (Exception e) {
            throw new RuntimeException("Hiba a fájl létrehozásakor: " + e.getMessage());
        }
    }

    public UserController.GetMyNameResponse getMyName(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Felhasználó nem található ID: " + email));

        return new UserController.GetMyNameResponse(user.getFirstName(), user.getLastName());
    }
}