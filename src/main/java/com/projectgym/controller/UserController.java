package com.projectgym.controller;

import com.projectgym.ApiResponse;
import com.projectgym.repository.UserProjection;
import com.projectgym.model.User;
import com.projectgym.service.AuthService;
import com.projectgym.service.EmailService;
import com.projectgym.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Controller
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService service;
    private final AuthService authService;
    private final EmailService emailService;

    public UserController(UserService service, AuthService authService, EmailService emailService) {
        this.service = service;
        this.authService = authService;
        this.emailService = emailService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerUser(@RequestBody User user) {
        service.registerUser(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Sikeres regisztráció! A hitelesítő email elküldve!"));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest, HttpServletResponse httpResponse) {
        LoginResponse response = authService.login(loginRequest);

        ResponseCookie  refreshTokenCookie = ResponseCookie.from("refreshToken", response.getRefreshToken())
                .httpOnly(true)
                .secure(true) // todo, ha deploy true, ha dev false
                .maxAge(60 * 60 * 24 * 30)
                .path("/")
                .sameSite("Lax")
                .build();

        httpResponse.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

        response.setRefreshToken(null);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/sendForgottenPassEmail")
    public ResponseEntity<ApiResponse> sendForgottenPassEmail(@RequestBody EmailRequest request) {
        emailService.SendPasswordResetEmail(request.getEmail());
        return ResponseEntity.ok(ApiResponse.success("Email elküldve"));
    }

    @PostMapping("/resetPassword/")
    public ResponseEntity<ApiResponse> changePassword(@CookieValue("refreshToken") String token, @RequestBody ResetPasswordRequest request) {
        String email = service.getEmailByRefreshToken(token);

        return ResponseEntity.ok(ApiResponse.success(authService.changePassword(email, request)));
    }

    @PostMapping("/forgottenPassword")
    public ResponseEntity<ApiResponse> resetPassword(@RequestBody ForgottenPasswordRequest request) {
        service.resetPassword(request);
        return ResponseEntity.ok(ApiResponse.success("Sikeres jelszóvisszaállítás"));
    }

    @GetMapping("/verify/{token}")
    public ResponseEntity<ApiResponse> verifyToken(@PathVariable String token) {
        service.verifyUser(token);
        return ResponseEntity.ok(ApiResponse.success("Sikeres aktiválás"));
    }

    @GetMapping("requests/getAllTrainer")
    public ResponseEntity<List<Trainers>> getAllTrainer(){
        return ResponseEntity.ok(service.getAllTrainer());
    }

    @GetMapping("/requests/userById/{id}")
    public ResponseEntity<UserProjection> getUserById(@PathVariable int id) {
        return ResponseEntity.ok(service.getUserById(id));
    }

    @GetMapping("/requests/getAllUser")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserProjection>> getAllUserForAdmin() {
        return ResponseEntity.ok(service.getAllUsers());
    }

    @PostMapping("/change/name")
    @PreAuthorize("hasRole('felhasználó') or hasRole('edző')")
    public ResponseEntity<ApiResponse> changeName(@CookieValue("refreshToken") String token, @RequestBody NameChangeRequest request) {
        String email = service.getEmailByRefreshToken(token);
        service.changeName(email, request.getFirstName(), request.getLastName());
        return ResponseEntity.ok(ApiResponse.success("Sikeres név változtatás"));
    }

    @PostMapping("/change/number")
    @PreAuthorize("hasRole('felhasználó') or hasRole('edző')")
    public ResponseEntity<ApiResponse> changeNumber(@CookieValue("refreshToken") String token, @RequestBody NumberRequest request) {
        String email = service.getEmailByRefreshToken(token);

        service.changeNumber(email, request.getNumber());
        return ResponseEntity.ok(ApiResponse.success("Sikeres telefonszám átállítás"));
    }

    @PostMapping("/change/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> changeRole(@RequestBody changeUserRole changeUser) {
        service.changeUserRole(changeUser);
        return ResponseEntity.ok(ApiResponse.success("Szerepkör módosítva"));
    }

    @GetMapping("get/pfp")
    public ResponseEntity<String> getProfilePicture(@CookieValue("refreshToken") String token) {
        String email = service.getEmailByRefreshToken(token);
        return ResponseEntity.ok(service.getpfp(email));
    }

    @DeleteMapping("/delete/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable String email) {
        service.deleteUser(email);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("user/delete")
    public ResponseEntity<ApiResponse> deleteMe(@CookieValue("refreshToken") String token){
        String currentUserEmail = service.getEmailByRefreshToken(token);
        service.deleteMe(currentUserEmail);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("change/pfp")
    @PreAuthorize("hasRole('felhasználó') or hasRole('edző')")
    public ResponseEntity<ApiResponse> updateProfilePicture(@CookieValue("refreshToken") String token, @RequestParam("file") MultipartFile file) {
        String currentUserEmail = service.getEmailByRefreshToken(token);

        String newImgUrl = service.changePfp(currentUserEmail, file);

        return ResponseEntity.ok(ApiResponse.success("Sikeres profilkép változatatás " + newImgUrl));
    }

    @PutMapping("change/description")
    @PreAuthorize("hasRole('edző')")
    public ResponseEntity<ApiResponse> changeDescription(@CookieValue("refreshToken") String token, @RequestBody String description) {
        String currentUserEmail = service.getEmailByRefreshToken(token);
         service.changeDescription(currentUserEmail, description);
        return ResponseEntity.ok(ApiResponse.success("Sikeres leírás változtatás"));
    }

    @PutMapping("change/wage")
    @PreAuthorize("hasRole('edző')")
    public ResponseEntity<ApiResponse> changeWage(@CookieValue("refreshToken") String token, @RequestBody int wage) {
        String currentUserEmail = service.getEmailByRefreshToken(token);
        service.changeWage(currentUserEmail, wage);
        return ResponseEntity.ok(ApiResponse.success("Sikeres óradíj változtatás"));
    }

    @Setter
    @Getter
    public static class LoginRequest {
        private String email;
        private String password;
        private String deviceInfo;

        public LoginRequest(String email, String password, String deviceInfo) {
            this.email = email;
            this.password = password;
            this.deviceInfo = deviceInfo;
        }

    }

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LoginResponse {
        private String accessToken;
        private String refreshToken;

    }

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NumberRequest {
        private String number;
    }

    @Setter
    @Getter
    public static class EmailRequest {
        private String email;
    }

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ForgottenPasswordRequest {
        private String token;
        private String password1;
        private String password2;
    }

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResetPasswordRequest {
        private String oldPassword;
        private String password1;
        private String password2;
    }

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class changeUserRole {
        private int id;
        private Integer roleId;
    }

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Trainers {
        private int trainerId;
        private String trainerName;
        private String profilePicture;
        private String description;
        private Integer hourlyRate;
    }

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NameChangeRequest {
        private String firstName;
        private String lastName;
    }

}
