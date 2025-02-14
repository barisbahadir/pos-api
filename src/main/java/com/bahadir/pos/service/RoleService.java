package com.bahadir.pos.service;

import com.bahadir.pos.entity.role.Role;
import com.bahadir.pos.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    // Rolleri listele
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Optional<Role> getRoleById(Long roleId) {
        return roleRepository.findById(roleId);
    }

    // Yeni rol oluştur
    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    // Rol güncelle
    public Role updateRole(Long roleId, Role updatedRole) {
        Optional<Role> existingRoleOpt = roleRepository.findById(roleId);

        if (existingRoleOpt.isPresent()) {
            Role existingRole = existingRoleOpt.get();
            existingRole.setName(updatedRole.getName());
            existingRole.setDescription(updatedRole.getDescription());
            return roleRepository.save(existingRole);
        } else {
            throw new IllegalArgumentException("Rol bulunamadı: " + roleId);
        }
    }

    // Rol sil
    public void deleteRole(Long roleId) {
        Optional<Role> existingRoleOpt = roleRepository.findById(roleId);

        if (existingRoleOpt.isPresent()) {
            roleRepository.deleteById(roleId);
        } else {
            throw new IllegalArgumentException("Rol bulunamadı: " + roleId);
        }
    }

    // Tüm rolleri sil
    public void deleteAllRoles() {
        roleRepository.deleteAll();
    }
}
