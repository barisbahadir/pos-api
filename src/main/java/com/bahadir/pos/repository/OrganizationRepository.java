package com.bahadir.pos.repository;

import com.bahadir.pos.entity.company.Company;
import com.bahadir.pos.entity.organization.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    List<Organization> findByCompany(Company company);

    Optional<Organization> findByName(String name); // Organizasyon adına göre arama

}
