package com.gymprojekt.projectgym.repository;

import com.gymprojekt.projectgym.model.StripePurchaseValidation;
import com.gymprojekt.projectgym.model.User;
import org.springframework.data.repository.CrudRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface StripePurchaseValidationRepository extends CrudRepository<StripePurchaseValidation, Long> {

    List<StripePurchaseValidation> findByUserId (Integer userId);

    List<StripePurchaseValidation> findByUser (User user);

    List<StripePurchaseValidation> findBySuccessfulPurchase(Boolean successfulPurchase);

    List<StripePurchaseValidation> findByUserAndSuccessfulPurchase (User user, Boolean successfulPurchase);

    List<StripePurchaseValidation> findByPurchaseDateBetween (Instant start, Instant end);

    Optional<StripePurchaseValidation> findTopByUserOrderByPurchaseDateDesc (User user);

}
