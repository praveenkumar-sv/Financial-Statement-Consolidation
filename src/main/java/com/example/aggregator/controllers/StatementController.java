package com.example.aggregator.controllers;

import com.example.aggregator.services.BankStatementService;
import com.example.aggregator.services.AWSService;
import com.opencsv.exceptions.CsvException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/statements")
public class StatementController {
private static final Logger logger= LoggerFactory.getLogger(StatementController.class);
    @Autowired
    private BankStatementService bankStatementService;

    @Autowired
    private AWSService awsService;

    @PostMapping("/generate")
    public ResponseEntity<String> generateStatement(@RequestParam Long userId, @RequestParam Long companyId, @RequestParam Long branchId,
                                            @RequestParam int transactionCount, @RequestParam boolean deleteAfterUpload) {
        try {
            String generateBankStatement = bankStatementService.generateBankStatement(userId, companyId, branchId, transactionCount, deleteAfterUpload);
            logger.info("Statement Generated | userID{},companyId{}|branchId{}|transactionCount{}",userId,companyId,branchId,transactionCount);
            return ResponseEntity.status(HttpStatus.CREATED).body(generateBankStatement);
        } catch (IOException e) {
            logger.error("Statement Generated | userID{},companyId{}|branchId{}|transactionCount{}",userId,companyId,branchId,transactionCount);
return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to Generate Statement");
        }
    }

    @GetMapping("/download")
    public ResponseEntity<String> downloadStatement(@RequestParam String key) {
        try {
            awsService.downloadFileToLocal(key);
            logger.info("File Downloaded successfully to local download folder");
            return ResponseEntity.status(HttpStatus.CREATED).body(key);
        } catch (IOException e) {
            logger.error("Getting Error downloading statement");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/parse")
    public String parseStatement(@RequestParam String filePath) {
        try {
            bankStatementService.parseAndSaveTransactions(filePath);
            return "Transactions parsed and saved successfully!";
        } catch (IOException | CsvException e) {
            return "Error parsing transactions: " + e.getMessage();
        }
    }
}
