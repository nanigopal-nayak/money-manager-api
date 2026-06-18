package com.moneymanager.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.moneymanager.dto.ExpenseDTO;
import com.moneymanager.entity.ProfileEntity;
import com.moneymanager.repository.ProfileRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
   private final ProfileRepository profileRepository;
   private final EmailService emailService;
   private final ExpenceService expenceService;
   
   @Value("${money.manager.frontend.url}")
   private String frontendUrl;
   
   @Scheduled(cron = "0 0 22 * * *", zone = "IST")
   public void sendDailyIncomeExpenceReminder() {
	   log.info("Job started: sendDailyIncomeExpenseReminder()");
	   List<ProfileEntity> profiles =profileRepository.findAll();
	   for (ProfileEntity profile : profiles) {

		    String body = "Hi " + profile.getFullName() + ",<br><br>"
		            + "This is a friendly reminder to add your income and expenses for today in Money Manager.<br><br>"
		            + "<a href='" + frontendUrl + "' "
		            + "style='display:inline-block;padding:10px 20px;"
		            + "background-color:#4CAF50;color:white;text-decoration:none;"
		            + "border-radius:5px;'>"
		            + "Open Money Manager"
		            + "</a>"
		            + "<br><br>Best regards,<br>Money Manager Team";
            emailService.sendEmail(profile.getEmail(), "Daily reminder Add Your income and expences", body);
		}
	   log.info("Job completed: sendDailyIncomeExpensereminder()");
   }
   @Scheduled(cron = "0 0 23 * * *", zone = "IST")
   public void senddailyExpenseSummary() {
	   List<ProfileEntity> profiles =profileRepository.findAll();
	   for (ProfileEntity profile : profiles) {
		   List<ExpenseDTO> todaysExpenses = expenceService.getExpencesForUserOnDate(profile.getId(), LocalDate.now());
		   
		   if(!todaysExpenses.isEmpty()) {
			   StringBuilder table = new StringBuilder();
			   table.append("<table border='1' style='border-collapse: collapse;'>");
			    table.append("<tr>");
			    table.append("<th>Name</th>");
			    table.append("<th>Amount</th>");
			    table.append("<th>Category</th>");
			    table.append("</tr>");
                
			    int i = 1;

			    for (ExpenseDTO expenseDTO : todaysExpenses) {

			        table.append("<tr>");

			        table.append("<td style='border:1px solid #ddd; padding:8px;'>")
			             .append(i++)
			             .append("</td>");

			        table.append("<td style='border:1px solid #ddd; padding:8px;'>")
			             .append(expenseDTO.getName())
			             .append("</td>");

			        table.append("<td style='border:1px solid #ddd; padding:8px;'>")
			             .append(expenseDTO.getAmount())
			             .append("</td>");

			        table.append("<td style='border:1px solid #ddd; padding:8px;'>")
			             .append(expenseDTO.getCategory())
			             .append("</td>");

			        table.append("</tr>");
			    }
			    table.append("</table>");
			    String body = "Hi "+profile.getFullName()+",<br/><br/> Here is a summary of your expences for today:<br/>"
			    		       + "<br/>"+table+"<br/><br/>Best regards, <br/>Money Manager Team";
			    
			    emailService.sendEmail(profile.getEmail(), "Your daily Expences summary", body);
		   }
		   log.info("Job completed: senddailyExpenseSummary()");
	   }
   }
}
