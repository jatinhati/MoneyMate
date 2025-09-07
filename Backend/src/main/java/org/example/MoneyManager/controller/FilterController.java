package org.example.MoneyManager.controller;

import lombok.RequiredArgsConstructor;
import org.example.MoneyManager.dto.ExpenseDTO;
import org.example.MoneyManager.dto.FilterDTO;
import org.example.MoneyManager.dto.IncomeDTO;
import org.example.MoneyManager.service.ExpenseService;
import org.example.MoneyManager.service.IncomeService;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("filter")
public class FilterController {

    private IncomeService incomeService;
    private ExpenseService expenseService;

    public ResponseEntity<?> filterTransactions(@RequestBody FilterDTO filterDTO) {
        LocalDate startDate = filterDTO.getStartDate() != null ? filterDTO.getStartDate() : LocalDate.MIN;
        LocalDate endDate = filterDTO.getEndDate() != null ? filterDTO.getEndDate() : LocalDate.now();
        String keyword = filterDTO.getKeyword() != null ? filterDTO.getKeyword() : "";
        String sortField = filterDTO.getSortField() != null ? filterDTO.getSortOrder() : "date";
        Sort.Direction direction = "desc".equalsIgnoreCase(filterDTO.getSortOrder()) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortField);
        if ("income".equals(filterDTO.getType())) {
            List<IncomeDTO> incomes = incomeService.filterIncomes(startDate, endDate, keyword, sort);
            return ResponseEntity.ok(incomes);
        } else if ("expense".equalsIgnoreCase(filterDTO.getType())) {
            List<ExpenseDTO> expenses = expenseService.filterExpenses(startDate, endDate, keyword, sort);
            return ResponseEntity.ok(expenses);
        } else {
            return ResponseEntity.badRequest().body("Invalid Type. Must be 'income' or 'expense'");
        }
    }

}
