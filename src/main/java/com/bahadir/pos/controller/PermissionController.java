package com.bahadir.pos.controller;

import com.bahadir.pos.entity.permission.Permission;
import com.bahadir.pos.exception.ApiException;
import com.bahadir.pos.service.PermissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/permission")
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    // İzinleri listele
    @GetMapping("/list")
    public ResponseEntity<List<Permission>> listAll() {
        List<Permission> permissions = permissionService.getAllPermissions();
        return ResponseEntity.ok(permissions);
    }

    @GetMapping("/list/{permissionId}")
    public ResponseEntity<Permission> listById(@PathVariable Long permissionId) {
        Optional<Permission> permissionResult = permissionService.getPermissionById(permissionId);
        if (!permissionResult.isPresent()) {
            throw new ApiException("İzin bulunamadi!");
        }
        return ResponseEntity.ok(permissionResult.get());
    }

    // Yeni izin oluştur
    @PostMapping("/add")
    public ResponseEntity<Permission> add(@RequestBody Permission permission) {
        Permission createdPermission = permissionService.createPermission(permission);
        return ResponseEntity.ok(createdPermission);
    }

    // İzin güncelle
    @PostMapping("/update/{permissionId}")
    public ResponseEntity<Permission> update(@PathVariable Long permissionId, @RequestBody Permission permission) {
        Permission updatedPermission = permissionService.updatePermission(permissionId, permission);
        return ResponseEntity.ok(updatedPermission);
    }

    // İzin sil
    @PostMapping("/delete/{permissionId}")
    public ResponseEntity<Boolean> delete(@PathVariable Long permissionId) {
        permissionService.deletePermission(permissionId);
        return ResponseEntity.ok(true);
    }

    // Tüm izinleri sil
    @PostMapping("/delete/all")
    public ResponseEntity<Boolean> deleteAll() {
        permissionService.deleteAllPermissions();
        return ResponseEntity.ok(true);
    }
}
