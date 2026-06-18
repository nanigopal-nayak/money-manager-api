package com.moneymanager.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moneymanager.dto.ExpenseDTO;
import com.moneymanager.dto.FilterDTO;
import com.moneymanager.dto.IncomeDTO;
import com.moneymanager.service.ExpenceService;
import com.moneymanager.service.IncomeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/filter")
public class FilterController {
       private final ExpenceService expenceService;
       private final IncomeService incomeService;
       
       @PostMapping
       public ResponseEntity<?> filterTransactions(@RequestBody FilterDTO filter){
    	   LocalDate startDate = filter.getStartDate() != null
    		        ? filter.getStartDate()
    		        : LocalDate.of(2000, 1, 1);
    	   LocalDate endDate = filter.getEndDate() != null ? filter.getEndDate() : LocalDate.now();
    	   String keyword = filter.getKeyword() != null ? filter.getKeyword(): "";
    	   String sortField = filter.getSortField() != null ? filter.getSortField() : "date";
    	   Sort.Direction direction = "desc".equalsIgnoreCase(filter.getSortOrder()) ? Sort.Direction.DESC : Sort.Direction.ASC;
    	   Sort sort = Sort.by(direction, sortField);
    	   if("income".equals(filter.getType())) {
    		  List<IncomeDTO> incomes = incomeService.filterExpenses(startDate, endDate, keyword);
    		  return ResponseEntity.ok(incomes);
    	   }else if("expence".equals(filter.getType())) {
    		  List<ExpenseDTO> expences = expenceService.filterExpenses(startDate, endDate, keyword);
    		  return ResponseEntity.ok(expences);
    	   }else {
    		   return ResponseEntity.badRequest().body("Invalid type. Must be 'income' or 'expence'");
    	   }
       }
}
