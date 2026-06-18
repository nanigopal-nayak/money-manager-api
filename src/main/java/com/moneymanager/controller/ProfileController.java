package com.moneymanager.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.moneymanager.dto.AuthDTO;
import com.moneymanager.dto.ProfileDTO;
import com.moneymanager.service.ProfileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;
    
    @PostMapping("/register")
    public ResponseEntity<ProfileDTO> registerProfile(@RequestBody ProfileDTO profileDTO){
    	ProfileDTO registeredProfile = profileService.registerProfile(profileDTO);
    	return ResponseEntity.status(HttpStatus.CREATED).body(registeredProfile);
    }
    
    @GetMapping("/activate")
    public ResponseEntity<String> activateProfile(@RequestParam String token){
    	boolean isActivate = profileService.activateProfile(token);
    	if(isActivate) {
    		return ResponseEntity.ok("Profile activate successfully");
    	} else {
    		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Activation token not found or alredy used");
    	}
    }
    
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody AuthDTO authDTO){
    	try {
    		if(!profileService.isAcountActive(authDTO.getEmail())) {
    			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
    					"message", "Account is not active. Please activate your account first"
    					));
    		}
    		Map<String, Object> response = profileService.authenticateAndGenerateToken(authDTO);
    		return ResponseEntity.ok(response);
    	}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
					   "message", e.getMessage()
					));
		}
    }
}
