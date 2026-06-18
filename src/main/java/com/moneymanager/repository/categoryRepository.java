package com.moneymanager.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moneymanager.entity.CategoryEntity;

public interface categoryRepository extends JpaRepository<CategoryEntity, Long>{
      
	//Select * from tbl_categories where profile_id =?
	List<CategoryEntity> findByProfileId(Long profileId);
	
	//select * from tbl_categories where id= ? and profileId= ?
	Optional<CategoryEntity> findByIdAndProfileId(Long id, Long profileId);
	
	//select * from tbl_categories where type =? and profile_id =?
	List<CategoryEntity> findByTypeAndProfileId(String type, Long profileId);
	
	Boolean existsByNameAndProfileId(String name, Long profileId);
}
