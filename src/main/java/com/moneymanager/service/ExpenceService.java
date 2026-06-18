package com.moneymanager.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.moneymanager.dto.ExpenseDTO;
import com.moneymanager.entity.CategoryEntity;
import com.moneymanager.entity.ExpenseEntity;
import com.moneymanager.entity.ProfileEntity;
import com.moneymanager.repository.ExpenseRepository;
import com.moneymanager.repository.categoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpenceService {
       private final categoryRepository categoryRepository;
       private final ExpenseRepository expenseRepository;
       private final ProfileService profileService;
       
       //Adds a new Expense to the database
       public ExpenseDTO addExpense(ExpenseDTO dto) {
    	   ProfileEntity profile = profileService.getCurrentProfile();
    	   CategoryEntity category = categoryRepository.findById(dto.getCategoryId())
    			   .orElseThrow(()-> new RuntimeException("Category Not Found"));
    	   ExpenseEntity newexpense = toEntity(dto, profile, category);
    	   newexpense = expenseRepository.save(newexpense);
    	   return toDTO(newexpense);
       }
       
       //Retrivers all expenses for current month/based on the start date and end date
       public List<ExpenseDTO> getCurrentMonthExpensesForCurrentUser() {
    	   ProfileEntity profile = profileService.getCurrentProfile();
    	   LocalDate now = LocalDate.now();
    	   LocalDate startDate = now.withDayOfMonth(1);
    	   LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());
    	   List<ExpenseEntity> list= expenseRepository.findByProfileIdAndDateBetween(profile.getId(), startDate, endDate);
    	   return list.stream().map(this::toDTO).toList();
       }
       
       //delete expense by id for current user
       public void deleteExpense(Long expenseId) {
    	   ProfileEntity profile = profileService.getCurrentProfile();
    	   ExpenseEntity entity =expenseRepository.findById(expenseId)
    	                    .orElseThrow(() -> new RuntimeException("Expense not found"));
    	   if(entity.getProfile().getId().equals(profile)) {
    		   throw new RuntimeException("Unauthorized to delete this expense");
    	   }
    	   expenseRepository.delete(entity);
       }
       
       //Get latest 5 expenses for current use
       public List<ExpenseDTO> getLatest5ExpensesForCurrentUser(){
    	   ProfileEntity profile = profileService.getCurrentProfile();
    	   List<ExpenseEntity> list= expenseRepository.findTop5ByProfileIdOrderByDateDesc(profile.getId());
    	   return list.stream().map(this::toDTO).toList();
       }
       
       //Get total expenses for current users
       public BigDecimal getTotalExpenseForCurrentUser() {
    	   ProfileEntity profile = profileService.getCurrentProfile();
    	   BigDecimal total = expenseRepository.findTotalExpenseByProfileId(profile.getId());
    	   return total != null ? total: BigDecimal.ZERO;
       }
       //Filter expenses
       public List<ExpenseDTO> filterExpenses(LocalDate startDate, LocalDate endDate, String keyword){
    	   ProfileEntity profile = profileService.getCurrentProfile();
    	   List<ExpenseEntity> list = expenseRepository.findByProfileIdAndDateBetweenAndNameContainingIgnoreCaseOrderByDateDesc
    			   (profile.getId(), startDate, endDate, keyword);
    	   return list.stream().map(this::toDTO).toList();
       }
       
       //Notifications
       public List<ExpenseDTO> getExpencesForUserOnDate(Long profileId, LocalDate date){
    	 List<ExpenseEntity> list = expenseRepository.findByProfileIdAndDate(profileId, date);
    	 return list.stream().map(this::toDTO).toList();
       }
       
       private ExpenseEntity toEntity(ExpenseDTO dto, ProfileEntity profile, CategoryEntity category) {
    	       return ExpenseEntity.builder()
    	    		   .name(dto.getName())
    	    		   .icon(dto.getIcon())
    	    		   .amount(dto.getAmount())
    	    		   .date(dto.getDate())
    	    		   .profile(profile)
    	    		   .category(category)
    	    		   .build();
       }
       
       private ExpenseDTO toDTO(ExpenseEntity entity) {
    	    return ExpenseDTO.builder()
    	            .id(entity.getId())
    	            .name(entity.getName())
    	            .icon(entity.getIcon())
    	            .categoryId(entity.getCategory() != null ? entity.getCategory().getId() : null)
    	            .categoryName(entity.getCategory() != null ? entity.getCategory().getName() : "N/A")
    	            .amount(entity.getAmount())
    	            .date(entity.getDate())
    	            .createdAt(entity.getCreatedAt())
    	            .updatedAt(entity.getUpdatedAt())
    	            .build();
    	}
}
