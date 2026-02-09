package com.gymprojekt.forevergym.repository;

import com.gymprojekt.forevergym.UserProjection;
import com.gymprojekt.forevergym.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    List<UserProjection> findAllProjectedBy();

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
}
