package com.gymprojekt.projectgym.repository;

import com.gymprojekt.projectgym.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    List<UserProjection> findAllProjectedBy();

    UserProjection findUserById(int id);

    // Milyen típusu a visszaérkező adat | milyen típusu a lekérdezés | elválasztó | hol keresse | mit keressen

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<User> findByVerificationToken(String token);

    List<User> findByValidUntilBefore(LocalDate date); // lejárt bérlet

    List<User> findByValidUntilAfter(LocalDate date); // aktív bérlet

    Optional<User> findByPhoneNumber(String phoneNumber);

    List<User> findByFirstName(String firstName);

    List<User> findByLastName(String lastName);

    List<User> findByFirstNameAndLastName(String firstName, String lastName);

    Optional<User> findByPassword(String password);

    Optional<User> findByResetPasswordToken(String token);

    void deleteByEmail(String email);

    @Query("SELECT u.id as trainerId, CONCAT( u.firstName, ' ', u.lastName) as trainerName FROM User u WHERE u.role.id = 2")
    List<TrainerProjection> findAllTrainers();
}
