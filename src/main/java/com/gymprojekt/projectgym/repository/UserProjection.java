package com.gymprojekt.projectgym.repository;

import com.gymprojekt.projectgym.model.Role;

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

