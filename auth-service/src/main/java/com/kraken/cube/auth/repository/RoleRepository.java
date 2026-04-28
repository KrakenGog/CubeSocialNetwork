package com.kraken.cube.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kraken.cube.auth.entity.Role;


public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query("select r from Role r where r.name = \"ROLE_USER\"")
    Role getUserRole();
}
