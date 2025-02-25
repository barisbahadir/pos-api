package com.bahadir.pos.controller;

import com.bahadir.pos.entity.company.Company;
import com.bahadir.pos.exception.ApiException;
import com.bahadir.pos.service.CompanyService;
import com.bahadir.pos.utils.ApiUtils;
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
    @GetMapping("/list/{id}")
    public ResponseEntity<Company> listById(@PathVariable String id) {
        Optional<Company> companyResult = companyService.getCompanyById(ApiUtils.getPathId(id));
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
    @PostMapping("/update/{id}")
    public ResponseEntity<Company> update(@PathVariable String id, @RequestBody Company company) {
        Company updatedCompany = companyService.updateCompany(ApiUtils.getPathId(id), company);
        return ResponseEntity.ok(updatedCompany);
    }

    // Şirket sil
    @PostMapping("/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable String id) {
        companyService.deleteCompany(ApiUtils.getPathId(id));
        return ResponseEntity.ok(true);
    }

    // Tüm şirketleri sil
    @PostMapping("/delete/all")
    public ResponseEntity<Boolean> deleteAll() {
        companyService.deleteAllCompanies();
        return ResponseEntity.ok(true);
    }
}
