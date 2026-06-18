package com.moneymanager.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moneymanager.service.DashboardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {
     private final DashboardService dashboardService;
     
     @GetMapping
     public ResponseEntity<Map<String, Object>> getDashboardDate(){
    	 Map<String, Object> dashboardData = dashboardService.getDashboardData();
    	 return ResponseEntity.ok(dashboardData);
     }
}
