package com.gymprojekt.forevergym;


import com.gymprojekt.forevergym.model.Role;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.Set;

public interface UserProjection {

    Integer getId();

    String getFirstName();

    String getLastName();

    String getEmail();

    String getPhoneNumber();

    Set<Role> getRole();

    LocalDate getValidUntil();

    Integer getHourlyWage();
}

