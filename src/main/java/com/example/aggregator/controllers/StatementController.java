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

import java.io.FileNotFoundException;
import java.io.IOException;

@RestController
@RequestMapping("/statements")
public class StatementController {
    private static final Logger logger = LoggerFactory.getLogger(StatementController.class);
    @Autowired
    private BankStatementService bankStatementService;

    @Autowired
    private AWSService awsService;

    @PostMapping("/generate")
    public ResponseEntity<String> generateStatement(@RequestParam Long userId, @RequestParam Long companyId, @RequestParam Long branchId,
                                                    @RequestParam int transactionCount, @RequestParam boolean deleteAfterUpload) {
        try {
            String generateBankStatement = bankStatementService.generateBankStatement(userId, companyId, branchId, transactionCount, deleteAfterUpload);
            logger.info(
                    "Statement Generated | userID{} | companyId{} | branchId{} | transactionCount{}",
                    userId, companyId, branchId, transactionCount
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(generateBankStatement);
        } catch (IOException e) {
            logger.error("Statement Generated | userID{},companyId{}|branchId{}|transactionCount{}", userId, companyId, branchId, transactionCount);
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
    public ResponseEntity<String> parseStatement(@RequestParam String fileName) {
        try {
            bankStatementService.parseAndSaveTransactions(fileName);
            logger.info("Transactions pasrsed and save to data delete .CSV FILE lcoal");
            return ResponseEntity.ok("Transactions parsed and saved successfully File : "+fileName);
        } catch (FileNotFoundException e) {
            logger.warn("there no file in local system please check once");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error parsing transactions: " + e.getMessage());
        }catch (SecurityException e){
            logger.error("internals server error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("internal server error ");
        }catch (Exception e){
            logger.error("parsing data to dB getting error ");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to pars the data to DB: "+fileName);
        }
    }
}
