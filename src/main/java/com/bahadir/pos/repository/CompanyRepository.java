package com.bahadir.pos.repository;

import com.bahadir.pos.entity.company.Company;
import com.bahadir.pos.entity.organization.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    Optional<Company> findByName(String name); // Şirket adına göre arama
}
