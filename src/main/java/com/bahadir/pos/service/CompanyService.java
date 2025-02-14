package com.bahadir.pos.service;

import com.bahadir.pos.entity.company.Company;
import com.bahadir.pos.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    // Şirketleri listele
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    public Optional<Company> getCompanyById(Long id) {
        return companyRepository.findById(id);
    }

    // Yeni şirket oluştur
    @Transactional
    public Company createCompany(Company company) {
        return companyRepository.save(company);
    }

    // Şirket güncelle
    @Transactional
    public Company updateCompany(Long id, Company companyDetails) {
        return companyRepository.findById(id).map(company -> {
            company.setName(companyDetails.getName());
            company.setStatus(companyDetails.getStatus());
            company.setOrderValue(companyDetails.getOrderValue());
            return companyRepository.save(company);
        }).orElseThrow(() -> new IllegalArgumentException("Şirket bulunamadı: " + id));
    }

    // Şirket sil
    @Transactional
    public void deleteCompany(Long id) {
        Optional<Company> existingCompany = companyRepository.findById(id);
        if (existingCompany.isPresent()) {
            companyRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Şirket bulunamadı: " + id);
        }
    }

    // Tüm şirketleri sil
    @Transactional
    public void deleteAllCompanies() {
        companyRepository.deleteAll();
    }
}