package com.gymprojekt.forevergym.repository;

import com.gymprojekt.forevergym.model.Role;
import com.gymprojekt.forevergym.model.RoleXUser;
import com.gymprojekt.forevergym.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RoleXUserRepository extends CrudRepository<RoleXUser,Integer> {

    List<RoleXUser> findByUser (User user);

    List<RoleXUser> findByRole (Role role);

    List<RoleXUser> findByUserId (Integer userId);

    boolean existsByUserAndRole (User user, Role role);

    void deleteByUserAndRole (User user, Role role);

}
