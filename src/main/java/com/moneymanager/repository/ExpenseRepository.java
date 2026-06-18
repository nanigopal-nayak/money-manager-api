package com.moneymanager.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.moneymanager.entity.ExpenseEntity;

public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Long>{
	
	//select * from tbl_expences where profile_id = ?1 order by date desc
    List<ExpenseEntity> findByProfileIdOrderByDateDesc(Long profileId);
    
    //select * from tbl_expences where profile_id = ?1 and order by date desc limit 5
    List<ExpenseEntity> findTop5ByProfileIdOrderByDateDesc(Long profileId);
    
    @Query("SELECT SUM(e.amount) From ExpenseEntity e WHERE e.profile.id = :profileId")
    BigDecimal findTotalExpenseByProfileId(@Param("profileId") Long profileId);
    
    //select * from tbl_expenses where profile_id =?1 and date between ?2 and ?3 and name like %%
    List<ExpenseEntity> findByProfileIdAndDateBetweenAndNameContainingIgnoreCaseOrderByDateDesc(
    	    Long profileId,
    	    LocalDate startDate,
    	    LocalDate endDate,
    	    String name
    	);
    
    List<ExpenseEntity>findByProfileIdAndDateBetween(Long profileId, LocalDate startDate, LocalDate endDate);
    
    //select * from tbl_expenses where profile_id =?1 And date =?2
    List<ExpenseEntity>findByProfileIdAndDate(Long profileId, LocalDate date);
    
}
