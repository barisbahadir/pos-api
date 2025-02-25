package com.bahadir.pos.controller;

import com.bahadir.pos.entity.organization.Organization;
import com.bahadir.pos.exception.ApiException;
import com.bahadir.pos.service.OrganizationService;
import com.bahadir.pos.utils.ApiUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/organization")
public class OrganizationController {

    private final OrganizationService organizationService;

    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    // Organizasyonları listele
    @GetMapping("/list")
    public ResponseEntity<List<Organization>> listAll() {
        List<Organization> organizations = organizationService.getAllOrganizations();
        return ResponseEntity.ok(organizations);
    }

    // Organizasyon bilgisi al
    @GetMapping("/list/{id}")
    public ResponseEntity<Organization> listById(@PathVariable String id) {
        Optional<Organization> organizationResult = organizationService.getOrganizationById(ApiUtils.getPathId(id));
        if (!organizationResult.isPresent()) {
            throw new ApiException("Organizasyon bulunamadi!");
        }
        return ResponseEntity.ok(organizationResult.get());
    }

    // Organizasyon oluştur
    @PostMapping("/add")
    public ResponseEntity<Organization> add(@RequestBody Organization organization) {
        Organization createdOrganization = organizationService.createOrganization(organization);
        return ResponseEntity.ok(createdOrganization);
    }

    // Organizasyon güncelle
    @PostMapping("/update/{id}")
    public ResponseEntity<Organization> update(@PathVariable String id, @RequestBody Organization organization) {
        Organization updatedOrganization = organizationService.updateOrganization(ApiUtils.getPathId(id), organization);
        return ResponseEntity.ok(updatedOrganization);
    }

    // Organizasyon sil
    @PostMapping("/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable String id) {
        organizationService.deleteOrganization(ApiUtils.getPathId(id));
        return ResponseEntity.ok(true);
    }

    // Tüm organizasyonları sil
    @PostMapping("/delete/all")
    public ResponseEntity<Boolean> deleteAll() {
        organizationService.deleteAllOrganizations();
        return ResponseEntity.ok(true);
    }
}
