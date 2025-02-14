package com.bahadir.pos.service;

import com.bahadir.pos.entity.organization.Organization;
import com.bahadir.pos.repository.OrganizationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    public OrganizationService(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    // Organizasyonları listele
    public List<Organization> getAllOrganizations() {
        return organizationRepository.findAll();
    }

    public Optional<Organization> getOrganizationById(Long organizationId) {
        return organizationRepository.findById(organizationId);
    }

    // Yeni organizasyon oluştur
    public Organization createOrganization(Organization organization) {
        return organizationRepository.save(organization);
    }

    // Organizasyon güncelle
    public Organization updateOrganization(Long organizationId, Organization updatedOrganization) {
        Optional<Organization> existingOrganizationOpt = organizationRepository.findById(organizationId);

        if (existingOrganizationOpt.isPresent()) {
            Organization existingOrganization = existingOrganizationOpt.get();
            existingOrganization.setName(updatedOrganization.getName());
            existingOrganization.setDescription(updatedOrganization.getDescription());
            return organizationRepository.save(existingOrganization);
        } else {
            throw new IllegalArgumentException("Organizasyon bulunamadı: " + organizationId);
        }
    }

    // Organizasyon sil
    public void deleteOrganization(Long organizationId) {
        Optional<Organization> existingOrganizationOpt = organizationRepository.findById(organizationId);

        if (existingOrganizationOpt.isPresent()) {
            organizationRepository.deleteById(organizationId);
        } else {
            throw new IllegalArgumentException("Organizasyon bulunamadı: " + organizationId);
        }
    }

    // Tüm organizasyonları sil
    public void deleteAllOrganizations() {
        organizationRepository.deleteAll();
    }
}
