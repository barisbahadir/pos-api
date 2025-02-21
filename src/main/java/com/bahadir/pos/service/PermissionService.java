package com.bahadir.pos.service;

import com.bahadir.pos.entity.permission.Permission;
import com.bahadir.pos.repository.PermissionRepository;
import org.springframework.stereotype.Service;

import java.util.*;

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

    // Permissions'ı sıralayıp parent-child ilişkisini kuran metod
    public List<Permission> getSortedPermissions(List<Permission> permissions) {
        Map<Long, Permission> permissionMap = new HashMap<>();

        // Permissionları permissionMap'e ekleyelim
        for (Permission permission : permissions) {
            permissionMap.put(permission.getId(), permission);
        }

        // Parent-Child ilişkisini kuruyoruz
        List<Permission> rootPermissions = new ArrayList<>();

        for (Permission permission : permissions) {
            if (permission.getParent() == null) {
                rootPermissions.add(permission); // Root elementleri alıyoruz
            } else {
                Permission parent = permissionMap.get(permission.getParent().getId());
                if (parent != null) {
                    parent.getChildren().add(permission); // Child öğeyi parent'a bağlıyoruz
                }
            }
        }

        // Sıralama işlemini yapıyoruz
        rootPermissions.sort(Comparator.comparingInt(Permission::getOrderValue)); // OrderValue'ya göre sıralıyoruz

        // Her bir root öğesinin children'ını da sıralıyoruz
        for (Permission root : rootPermissions) {
            sortChildren(root); // Alt öğeleri sıralıyoruz
        }

        return rootPermissions;
    }

    // Alt öğeleri sıralamak için recursive metod
    private void sortChildren(Permission parent) {
        parent.getChildren().sort(Comparator.comparingInt(Permission::getOrderValue)); // OrderValue'ya göre alt öğeleri sıralıyoruz
        for (Permission child : parent.getChildren()) {
            sortChildren(child); // Alt öğelerin alt öğelerini sıralıyoruz
        }
    }
}
