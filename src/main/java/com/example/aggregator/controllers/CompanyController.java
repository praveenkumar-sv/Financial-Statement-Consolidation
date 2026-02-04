package com.example.aggregator.controllers;

import com.example.aggregator.models.Company;
import com.example.aggregator.services.CompanyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/company")
public class CompanyController {
private static final Logger logger= LoggerFactory.getLogger(CompanyController.class);
    @Autowired
    private CompanyService companyService;

    @PostMapping("/create")
    public ResponseEntity<String> createCompany(@RequestBody Company company) {
        try {
            String result = companyService.createCompany(company);
            logger.info("Company created successfully companyName{}|companyId{}",company.getCompanyName(),company.getCompanyId());
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            logger.error("Failed to create company companyName{}",company.getCompanyName());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create company");
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCompanyById(@PathVariable Long id) {
        Company company = companyService.getCompanyById(id);
        if (company == null) {
            logger.warn("Company not found |companyId{}",id);
            return new ResponseEntity<>("Company not found", HttpStatus.NOT_FOUND);
        }
        logger.info("Company details Found companyName{}|companyId{}",company.getCompanyId(),company.getCompanyId());
        return new ResponseEntity<>(company, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Company>> getAllCompanies() {
        try {
            List<Company> companies = companyService.getAllCompanies();
            logger.info("Get The list of all company Details companyName{}|companyID{}",companies);
            return new ResponseEntity<>(companies, HttpStatus.OK);
        }catch (Exception e){
            logger.error("Company Details Not Found");
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
