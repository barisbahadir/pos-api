package com.bahadir.pos.controller;

import com.bahadir.pos.entity.company.Company;
import com.bahadir.pos.exception.ApiException;
import com.bahadir.pos.service.CompanyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/company")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    // Şirketleri listele
    @GetMapping("/list")
    public ResponseEntity<List<Company>> listAll() {
        List<Company> companies = companyService.getAllCompanies();
        return ResponseEntity.ok(companies);
    }

    // Şirket bilgisi al
    @GetMapping("/list/{companyId}")
    public ResponseEntity<Company> listById(@PathVariable Long companyId) {
        Optional<Company> companyResult = companyService.getCompanyById(companyId);
        if (!companyResult.isPresent()) {
            throw new ApiException("Şirket bulunamadi!");
        }
        return ResponseEntity.ok(companyResult.get());
    }

    // Şirket oluştur
    @PostMapping("/add")
    public ResponseEntity<Company> add(@RequestBody Company company) {
        Company createdCompany = companyService.createCompany(company);
        return ResponseEntity.ok(createdCompany);
    }

    // Şirket güncelle
    @PostMapping("/update/{companyId}")
    public ResponseEntity<Company> update(@PathVariable Long companyId, @RequestBody Company company) {
        Company updatedCompany = companyService.updateCompany(companyId, company);
        return ResponseEntity.ok(updatedCompany);
    }

    // Şirket sil
    @PostMapping("/delete/{companyId}")
    public ResponseEntity<Boolean> delete(@PathVariable Long companyId) {
        companyService.deleteCompany(companyId);
        return ResponseEntity.ok(true);
    }

    // Tüm şirketleri sil
    @PostMapping("/delete/all")
    public ResponseEntity<Boolean> deleteAll() {
        companyService.deleteAllCompanies();
        return ResponseEntity.ok(true);
    }
}
