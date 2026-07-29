package com.rea.express.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.rea.express.POJO.ERole;
import com.rea.express.POJO.Role;

import java.util.Optional;

public interface RoleDao extends JpaRepository<Role, Integer> {

    Optional<Role> findByName(ERole name);
}
