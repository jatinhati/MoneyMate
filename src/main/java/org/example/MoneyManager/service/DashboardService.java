package org.example.MoneyManager.service;

import lombok.RequiredArgsConstructor;
import org.example.MoneyManager.dto.ExpenseDTO;
import org.example.MoneyManager.dto.IncomeDTO;
import org.example.MoneyManager.dto.RecentTransactionDTO;
import org.example.MoneyManager.entity.ProfileEntity;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.DoubleStream.concat;

@Service
@RequiredArgsConstructor

public class DashboardService {

    private final ExpenseService expenseService;
    private final IncomeService incomeService;
    private final ProfileService profileService;

    public Map<String, Object> getDashboardData() {
        ProfileEntity profile = profileService.getCurrentProfile();
        Map<String, Object> returnValue = new LinkedHashMap<>();
        List<IncomeDTO> latestIncomes = incomeService.getLatest5IncomesForCurrentUser();
        List<ExpenseDTO> latestExpenses = expenseService.getLatest5ExpensesForCurrentUser();
List<RecentTransactionDTO> recentTransactions =
    Stream.concat(
        latestIncomes.stream().map(income ->
            RecentTransactionDTO.builder()
                .id(income.getId())
                .profileId(profile.getId())
                .icon(income.getIcon())
                .name(income.getName())
                .amount(income.getAmount())
                .date(income.getDate())
                .createdAt(income.getCreatedAt())
                .updatedAt(income.getUpdatedAt())
                .type("income")
                .build()
        ),
        latestExpenses.stream().map(expense ->
            RecentTransactionDTO.builder()
                .id(expense.getId())
                .profileId(profile.getId())
                .icon(expense.getIcon())
                .name(expense.getName())
                .amount(expense.getAmount())
                .date(expense.getDate())
                .createdAt(expense.getCreatedAt())
                .updatedAt(expense.getUpdatedAt())
                .type("expense")
                .build()
        )
    ).sorted((a, b) -> {
        int cmp = b.getDate().compareTo(a.getDate());
        if(cmp == 0 && a.getCreatedAt() != null && b.getCreatedAt() != null){
            return b.getCreatedAt().compareTo(a.getCreatedAt());
        }
        return cmp;
    }).toList();
returnValue.put("total balance",
        incomeService.getTotalIncomesForCurrentUser().subtract(expenseService.getTotalExpensesForCurrentUser()));
returnValue.put("total incomes", incomeService.getTotalIncomesForCurrentUser());
returnValue.put("total expenses", expenseService.getTotalExpensesForCurrentUser());
        returnValue.put("recent 5 expenses", latestExpenses);
        returnValue.put("recent 5 incomes", latestIncomes);
        returnValue.put("recent transactions", recentTransactions);
        return returnValue;

    }
}
