package com.bahadir.pos.repository;

import com.bahadir.pos.entity.permission.Permission;
import com.bahadir.pos.entity.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name); // Role adına göre arama

}
