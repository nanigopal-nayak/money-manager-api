package com.moneymanager.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.moneymanager.dto.IncomeDTO;
import com.moneymanager.entity.CategoryEntity;
import com.moneymanager.entity.IncomeEntity;
import com.moneymanager.entity.ProfileEntity;
import com.moneymanager.repository.IncomeRepository;
import com.moneymanager.repository.categoryRepository;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class IncomeService {
	private final categoryRepository categoryRepository;
    private final IncomeRepository incomeRepository;
    private final ProfileService profileService;
    
    
  //Adds a new Income to the database
    public IncomeDTO addIncome(IncomeDTO dto) {
 	   ProfileEntity profile = profileService.getCurrentProfile();
 	   CategoryEntity category = categoryRepository.findById(dto.getCategoryId())
 			   .orElseThrow(()-> new RuntimeException("Category Not Found"));
 	   IncomeEntity newexpense = toEntity(dto, profile, category);
 	   newexpense = incomeRepository.save(newexpense);
 	   return toDTO(newexpense);
    }
  //Retrivers all incomes for current month/based on the start date and end date
    public List<IncomeDTO> getCurrentMonthIncomeForCurrentUser() {
 	   ProfileEntity profile = profileService.getCurrentProfile();
 	   LocalDate now = LocalDate.now();
 	   LocalDate startDate = now.withDayOfMonth(1);
 	   LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());
 	   List<IncomeEntity> list= incomeRepository.findByProfileIdAndDateBetween(profile.getId(), startDate, endDate);
 	   return list.stream().map(this::toDTO).toList();
    }
    
    //delete Income by id for current user
    public void deleteIncome(Long incomeId) {
 	   ProfileEntity profile = profileService.getCurrentProfile();
 	   IncomeEntity entity =incomeRepository.findById(incomeId)
 	                    .orElseThrow(() -> new RuntimeException("income not found"));
 	   if(entity.getProfile().getId().equals(profile)) {
 		   throw new RuntimeException("Unauthorized to delete this income");
 	   }
 	  incomeRepository.delete(entity);
    }
    
  //Get latest 5 income for current use
    public List<IncomeDTO> getLatest5IncomesForCurrentUser(){
 	   ProfileEntity profile = profileService.getCurrentProfile();
 	   List<IncomeEntity> list= incomeRepository.findTop5ByProfileIdOrderByDateDesc(profile.getId());
 	   return list.stream().map(this::toDTO).toList();
    }
    
    //Get total income for current users
    public BigDecimal getTotalIncomeForCurrentUser() {
 	   ProfileEntity profile = profileService.getCurrentProfile();
 	   BigDecimal total = incomeRepository.findTotalIncomeByProfileId(profile.getId());
 	   return total != null ? total: BigDecimal.ZERO;
    }
    
  //Filter Incomes
    public List<IncomeDTO> filterExpenses(LocalDate startDate, LocalDate endDate, String keyword){
 	   ProfileEntity profile = profileService.getCurrentProfile();
 	   List<IncomeEntity> list = incomeRepository.findByProfileIdAndDateBetweenAndNameContainingIgnoreCaseOrderByDateDesc
 			   (profile.getId(), startDate, endDate, keyword);
 	   return list.stream().map(this::toDTO).toList();
    }
    
    private IncomeEntity toEntity(IncomeDTO dto, ProfileEntity profile, CategoryEntity category) {
 	       return IncomeEntity.builder()
 	    		   .name(dto.getName())
 	    		   .icon(dto.getIcon())
 	    		   .amount(dto.getAmount())
 	    		   .date(dto.getDate())
 	    		   .profile(profile)
 	    		   .category(category)
 	    		   .build();
    }
    
    private IncomeDTO toDTO(IncomeEntity entity) {
 	    return IncomeDTO.builder()
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
