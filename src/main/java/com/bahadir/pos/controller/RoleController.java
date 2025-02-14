package com.bahadir.pos.controller;

import com.bahadir.pos.entity.role.Role;
import com.bahadir.pos.exception.ApiException;
import com.bahadir.pos.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/role")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    // Rolleri listele
    @GetMapping("/list")
    public ResponseEntity<List<Role>> listAll() {
        List<Role> roles = roleService.getAllRoles();
        return ResponseEntity.ok(roles);
    }

    // Rol bilgisi al
    @GetMapping("/list/{roleId}")
    public ResponseEntity<Role> listById(@PathVariable Long roleId) {
        Optional<Role> roleResult = roleService.getRoleById(roleId);
        if (!roleResult.isPresent()) {
            throw new ApiException("Rol bulunamadi!");
        }
        return ResponseEntity.ok(roleResult.get());
    }

    // Yeni rol oluştur
    @PostMapping("/add")
    public ResponseEntity<Role> add(@RequestBody Role role) {
        Role createdRole = roleService.createRole(role);
        return ResponseEntity.ok(createdRole);
    }

    // Rol güncelle
    @PostMapping("/update/{roleId}")
    public ResponseEntity<Role> update(@PathVariable Long roleId, @RequestBody Role role) {
        Role updatedRole = roleService.updateRole(roleId, role);
        return ResponseEntity.ok(updatedRole);
    }

    // Rol sil
    @PostMapping("/delete/{roleId}")
    public ResponseEntity<Boolean> delete(@PathVariable Long roleId) {
        roleService.deleteRole(roleId);
        return ResponseEntity.ok(true);
    }

    // Tüm rolleri sil
    @PostMapping("/delete/all")
    public ResponseEntity<Boolean> deleteAll() {
        roleService.deleteAllRoles();
        return ResponseEntity.ok(true);
    }
}
