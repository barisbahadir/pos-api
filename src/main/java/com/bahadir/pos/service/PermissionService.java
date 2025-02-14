package com.bahadir.pos.service;

import com.bahadir.pos.entity.permission.Permission;
import com.bahadir.pos.repository.PermissionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PermissionService {

    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    // İzinleri listele
    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }

    public Optional<Permission> getPermissionById(Long permissionId) {
        return permissionRepository.findById(permissionId);
    }

    // Yeni izin oluştur
    public Permission createPermission(Permission permission) {
        return permissionRepository.save(permission);
    }

    // İzin güncelle
    public Permission updatePermission(Long permissionId, Permission updatedPermission) {
        Optional<Permission> existingPermissionOpt = permissionRepository.findById(permissionId);

        if (existingPermissionOpt.isPresent()) {
            Permission existingPermission = existingPermissionOpt.get();
            existingPermission.setName(updatedPermission.getName());
            existingPermission.setDescription(updatedPermission.getDescription());
            return permissionRepository.save(existingPermission);
        } else {
            throw new IllegalArgumentException("İzin bulunamadı: " + permissionId);
        }
    }

    // İzin sil
    public void deletePermission(Long permissionId) {
        Optional<Permission> existingPermissionOpt = permissionRepository.findById(permissionId);

        if (existingPermissionOpt.isPresent()) {
            permissionRepository.deleteById(permissionId);
        } else {
            throw new IllegalArgumentException("İzin bulunamadı: " + permissionId);
        }
    }

    // Tüm izinleri sil
    public void deleteAllPermissions() {
        permissionRepository.deleteAll();
    }
}
