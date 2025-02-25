package com.bahadir.pos.controller;

import com.bahadir.pos.entity.permission.Permission;
import com.bahadir.pos.exception.ApiException;
import com.bahadir.pos.service.PermissionService;
import com.bahadir.pos.utils.ApiUtils;
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

    @GetMapping("/list/{id}")
    public ResponseEntity<Permission> listById(@PathVariable String id) {
        Optional<Permission> permissionResult = permissionService.getPermissionById(ApiUtils.getPathId(id));
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
    @PostMapping("/update/{id}")
    public ResponseEntity<Permission> update(@PathVariable String id, @RequestBody Permission permission) {
        Permission updatedPermission = permissionService.updatePermission(ApiUtils.getPathId(id), permission);
        return ResponseEntity.ok(updatedPermission);
    }

    // İzin sil
    @PostMapping("/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable String id) {
        permissionService.deletePermission(ApiUtils.getPathId(id));
        return ResponseEntity.ok(true);
    }

    // Tüm izinleri sil
    @PostMapping("/delete/all")
    public ResponseEntity<Boolean> deleteAll() {
        permissionService.deleteAllPermissions();
        return ResponseEntity.ok(true);
    }
}
