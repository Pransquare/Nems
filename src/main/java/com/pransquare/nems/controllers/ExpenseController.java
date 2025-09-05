package com.pransquare.nems.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.pransquare.nems.entities.ExpenseDetails;
import com.pransquare.nems.entities.ExpenseEntity;
import com.pransquare.nems.exceptions.ResourceNotFoundException;
import com.pransquare.nems.models.ExpenseSearchRequestModel;
import com.pransquare.nems.models.ExpenseUpdateModal;
import com.pransquare.nems.services.ExpenseService;
import com.pransquare.nems.utils.PageableResponse;

@RestController
@RequestMapping("/Pransquare/nems/expense")
public class ExpenseController {

    private ExpenseService expenseService;

    ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping(value = "/save")
    public ResponseEntity<ExpenseEntity> saveOrUpdateExpenses(@RequestBody ExpenseEntity expenseEntity) {
        try {
            // Ensure each ExpenseDetail is linked with the parent ExpenseEntity
            if (expenseEntity.getExpenseDetails() != null) {
                for (ExpenseDetails expenseDetail : expenseEntity.getExpenseDetails()) {
                    // Set the ExpenseEntity for each ExpenseDetail
                    expenseDetail.setExpense(expenseEntity);
                }
            }

            // Now save or update the expense entity with the expense details
            ExpenseEntity savedExpense = expenseService.saveOrUpdateExpenses(expenseEntity);

            return ResponseEntity.ok(savedExpense); // Return the saved expense entity
        } catch (Exception e) {
            // Catch the error and throw a custom exception
            throw new ResourceNotFoundException("Error occurred while saving the expense: " + e.getMessage());
        }
    }

    @PostMapping(value = "/billUpload", consumes = "multipart/form-data")
    public ResponseEntity<List<String>> uploadBill(@RequestParam(required = true) List<MultipartFile> file,
            @RequestParam(required = true) String employeeCode, @RequestParam(required = false) String type) {
        try {
            List<String> response = expenseService.uploadBill(file, employeeCode, type);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IOException e) {
            throw new ResourceNotFoundException("Error while uploading file");
        } catch (IllegalArgumentException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @PutMapping("/removeBillUrl")
    public ResponseEntity<String> removeBillUrl(@RequestParam(required = true) String employeeCode){
        try {
            expenseService.removeBillUrl(employeeCode);
            return new ResponseEntity<>("Bill URL removed successfully", HttpStatus.OK);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Error while removing bill URL: " + e.getMessage());
        }
    }

    @PostMapping("/search")
    public ResponseEntity<PageableResponse<ExpenseEntity>> getExpenses(
            @RequestBody ExpenseSearchRequestModel request) {

        // Set default values if not provided
        String sortBy = request.getSortBy() != null ? request.getSortBy() : "entryDate";
        String sortOrder = request.getSortOrder() != null ? request.getSortOrder() : "DESC";
        int page = request.getPage();
        int size = request.getSize();

        // Handle sort order
        Sort.Order sortOrderDirection = "DESC".equalsIgnoreCase(sortOrder)
                ? Sort.Order.desc(sortBy)
                : Sort.Order.asc(sortBy);

        Sort sort = Sort.by(sortOrderDirection);

        // Fetch expenses with filters and pagination
        PageableResponse<ExpenseEntity> expenses = expenseService.getExpensesWithFilters(
                request.getEmployeeId(), request.getManagerId(), request.getStatus(),
                PageRequest.of(page, size, sort));

        return ResponseEntity.ok(expenses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<ExpenseEntity>> getExpenseById(@PathVariable Integer id) {
        Optional<ExpenseEntity> expense = expenseService.getExpenseById(id);
        return ResponseEntity.ok(expense);
    }

    @PutMapping("/updateExpense")
    public ResponseEntity<ExpenseEntity> updateExpense(@RequestBody ExpenseUpdateModal modal) {
        ExpenseEntity expense = expenseService.updateExpense(modal);
        return ResponseEntity.ok(expense);
    }

    @PutMapping("/updateExpenseBill")
    public ResponseEntity<ExpenseDetails> changeBillStatus(@RequestBody ExpenseUpdateModal modal) {
        ExpenseDetails expense = expenseService.updateBillStatus(modal);
        return ResponseEntity.ok(expense);
    }

}
