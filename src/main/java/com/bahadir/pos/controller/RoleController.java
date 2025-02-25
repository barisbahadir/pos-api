package com.bahadir.pos.controller;

import com.bahadir.pos.entity.role.Role;
import com.bahadir.pos.exception.ApiException;
import com.bahadir.pos.service.RoleService;
import com.bahadir.pos.utils.ApiUtils;
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
    @GetMapping("/list/{id}")
    public ResponseEntity<Role> listById(@PathVariable String id) {
        Optional<Role> roleResult = roleService.getRoleById(ApiUtils.getPathId(id));
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
    @PostMapping("/update/{id}")
    public ResponseEntity<Role> update(@PathVariable String id, @RequestBody Role role) {
        Role updatedRole = roleService.updateRole(ApiUtils.getPathId(id), role);
        return ResponseEntity.ok(updatedRole);
    }

    // Rol sil
    @PostMapping("/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable String id) {
        roleService.deleteRole(ApiUtils.getPathId(id));
        return ResponseEntity.ok(true);
    }

    // Tüm rolleri sil
    @PostMapping("/delete/all")
    public ResponseEntity<Boolean> deleteAll() {
        roleService.deleteAllRoles();
        return ResponseEntity.ok(true);
    }
}
