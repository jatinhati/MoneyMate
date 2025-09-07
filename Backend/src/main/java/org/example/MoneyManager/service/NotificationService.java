package org.example.MoneyManager.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.MoneyManager.dto.ExpenseDTO;
import org.example.MoneyManager.entity.ProfileEntity;
import org.example.MoneyManager.repositories.ProfileRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final ProfileRepository profileRepository;
    private final EmailService emailService;
    private final ExpenseService expenseService;

    @Value("${money.manager.frontend.url}")
    private String frontendUrl;

    @Scheduled(cron = "0 0 0 22 * *",zone = "IST") // Every day at 10 PM IST
    public void sendDailyIncomeExpenseReminder() {
        log.info("Job started: sendDailyIncomeExpenseReminder");
        List<ProfileEntity> profiles = profileRepository.findAll();
        for (ProfileEntity profile : profiles) {
            String body = "Hi " + profile.getFullName() + ",<br><br>" +
                    "This is a gentle reminder to log your daily income and expenses. Keeping track of your finances is crucial for effective money management.<br><br>" +
                    "You can log your income and expenses by clicking the button below:<br>" +
                    "<a href='" + frontendUrl + "' style='display:inline-block;padding:10px 24px;background-color:#4CAF50;color:#fff;text-decoration:none;border-radius:5px;font-weight:bold;font-size:16px;margin-top:10px;'>Go to MoneyMate App</a><br><br>" +
                    "Thank you for using our service!<br><br>" +
                    "Best regards,<br>" +
                    "Your friend MoneyMate ❤️";
            emailService.sendEmail(profile.getEmail(), "Daily Reminder: Log Your Income and Expenses", body);
        }
        log.info("Job completed: sendDailyIncomeExpenseReminder");
    }

    @Transactional
    @Scheduled(cron = "0 * * * * *",zone = "IST") // Every day at 11 PM IST
    public void sendDailyExpenseSummary(){
        log.info("Job started: sendDailyExpenseSummary");
        List<ProfileEntity> profiles = profileRepository.findAll();
        for(ProfileEntity profile: profiles){
            List<ExpenseDTO> todayExpenses = expenseService.getExpensesForUserOnDate(profile.getId(), LocalDate.now());
            if (!todayExpenses.isEmpty()){
                StringBuilder table = new StringBuilder();
                table.append("<table style='border-collapse:collapse;width:100%;'>");
                table.append("<tr style='background-color:#f2f2f2;'><th style='border:1px solid #ddd;padding:8px;'>S.No</th>");
                table.append("<th style='border:1px solid #ddd;padding:8px;'>Category</th>");
                table.append("<th style='border:1px solid #ddd;padding:8px;'>Description</th>");
                table.append("<th style='border:1px solid #ddd;padding:8px;'>Amount</th></tr>");

                int i = 1;
                for(ExpenseDTO expense : todayExpenses) {
                    log.info("Processing expense: {}", expense);
                    table.append("<tr>");
                    table.append("<td style='border:1px solid #ddd;padding:8px;'>").append(i++).append("</td>");
                    table.append("<td style='border:1px solid #ddd;padding:8px;'>").append(expense.getName()).append("</td>");
                    table.append("<td style='border:1px solid #ddd;padding:8px;'>").append(expense.getAmount()).append("</td>");
                    table.append("<td style='border:1px solid #ddd;padding:8px;'>").append(expense.getCategoryId()).append("</td>");
                    table.append("</tr>");
                }
                table.append("</table>");
                String subject = "Daily Expense Summary - " + LocalDate.now();
                String body = "Hi,Here's your daily expense summary:\n\n" + table.toString();
                emailService.sendEmail(profile.getEmail(), subject, body);
                log.info("Daily expense summary sent to: {}", profile.getEmail());
            }
        }
        log.info("Job completed: sendDailyExpenseSummary");
    }

}
