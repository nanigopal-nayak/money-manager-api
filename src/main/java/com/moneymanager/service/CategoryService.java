package com.moneymanager.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.moneymanager.dto.CategoryDTO;
import com.moneymanager.entity.CategoryEntity;
import com.moneymanager.entity.ProfileEntity;
import com.moneymanager.repository.categoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

	private final ProfileService profileService;
	private final categoryRepository categoryRepository;
	
	//save category
	public CategoryDTO saveCategory(CategoryDTO categoryDTO) {
		
		ProfileEntity profile= profileService.getCurrentProfile();
		if(categoryRepository.existsByNameAndProfileId(categoryDTO.getName(),profile.getId())) {
			throw new RuntimeException("Category with this Name Alredy exist");
		}
		CategoryEntity neCategory = toEntity(categoryDTO, profile);
		neCategory = categoryRepository.save(neCategory);
		return toDTO(neCategory);
	}
	
	// Get categories for current user
	public List<CategoryDTO> getCategoriesForCurrentUser() {

	    ProfileEntity profile = profileService.getCurrentProfile();

	    List<CategoryEntity> categories =
	            categoryRepository.findByProfileId(profile.getId());

	    return categories.stream()
	            .map(this::toDTO)
	            .toList();
	}
	
	// Get categories by type for current user
	public List<CategoryDTO> getCategoriesByTypeForCurrentUser(String type) {

	    ProfileEntity profile = profileService.getCurrentProfile();

	    List<CategoryEntity> entities =
	            categoryRepository.findByTypeAndProfileId(
	                    type,
	                    profile.getId()
	            );

	    return entities.stream()
	            .map(this::toDTO)
	            .toList();
	}
	
	public CategoryDTO updateCategory(Long categoryId, CategoryDTO dto) {
		ProfileEntity profile = profileService.getCurrentProfile();
		CategoryEntity existingCategory = categoryRepository.findByIdAndProfileId(categoryId, profile.getId())
		                  .orElseThrow(() -> new RuntimeException("Category not found or not accessible"));
		existingCategory.setName(dto.getName());
		existingCategory.setIcon(dto.getIcon());
		existingCategory.setType(dto.getType());
		existingCategory = categoryRepository.save(existingCategory);
		return toDTO(existingCategory);
	}
	
	private CategoryEntity toEntity(CategoryDTO categoryDTO, ProfileEntity profile) {

	    return CategoryEntity.builder()
	            .name(categoryDTO.getName())
	            .icon(categoryDTO.getIcon())
	            .profile(profile)
	            .type(categoryDTO.getType())
	            .build();
	}

	private CategoryDTO toDTO(CategoryEntity entity) {

	    return CategoryDTO.builder()
	            .id(entity.getId())
	            .name(entity.getName())
	            .icon(entity.getIcon())
	            .type(entity.getType())
	            .profileId(entity.getProfile() != null ? entity.getProfile().getId(): null)
	            .createdAt(entity.getCreatedAt())
	            .updatedAt(entity.getUpdatedAt())
	            .build();
	}
}
