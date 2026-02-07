package com.example.aggregator.controllers;

import com.example.aggregator.models.Branch;
import com.example.aggregator.services.BranchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/branches")
public class BranchController {
private static final Logger logger= LoggerFactory.getLogger(BranchController.class);
    @Autowired
    private BranchService branchService;

    @PostMapping("/create")
    public ResponseEntity<Branch> createBranch(@RequestBody Branch branch) {
        logger.info("Create Branch | benachName{}|companyId{}",branch.getBranchName(),branch.getCompany().getCompanyId());
        try {
            Branch newBranch = branchService.createBranch(branch);
            logger.info("new branch Created |branchName{} |branchId{}",branch.getBranchName(),branch.getBranchId());
            return ResponseEntity.status(HttpStatus.CREATED).body(newBranch);
        } catch (Exception e) {
            logger.error("Failed to create new Branch | branchName{}|companyId{}",branch.getBranchName(),branch.getCompany().getCompanyId());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Branch> getBranchById(@PathVariable Long id) {
        logger.info("find branch details using branch ID branchId{}",id);
        Branch branchById = branchService.getBranchById(id);
        if (branchById ==null){
logger.warn("branch details not found");
return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        logger.info("Found the Branch details ");
        return ResponseEntity.status(HttpStatus.FOUND).body(branchById);
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<Branch>> getBranchesByCompanyId(@PathVariable Long companyId) {
        logger.info("get all branch details companyId{}",companyId);
List<Branch>branches=branchService.getBranchesByCompanyId(companyId);
if (branches ==null || branches.isEmpty()){
    logger.warn("branch details not found{}", companyId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
}
logger.info("Get all branch details branchname{}|branchID{}");
return ResponseEntity.status(HttpStatus.OK).body(branches);

    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteBranch(@PathVariable Long id){
        try {
            branchService.deleteBranchById(id);
            logger.info("Branch deletion are completed successfully :{}", id);
            return ResponseEntity.status(HttpStatus.OK).body("Branch delete successfully : "+id);
        } catch (Exception e) {
            logger.warn("Branch details Not Found :{}", id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Branch details not found : "+id
            );
        }

    }
}
