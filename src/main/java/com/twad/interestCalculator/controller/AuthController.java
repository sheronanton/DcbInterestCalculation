package com.twad.interestCalculator.controller;



import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.twad.interestCalculator.dto.LoginRequest;
import com.twad.interestCalculator.repo.AppUserRepository;



@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/intCalc/api")
public class AuthController {

    private final AppUserRepository userRepo;

    public AuthController(AppUserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        return userRepo.findByUsername(request.getUsername())
                .filter(u -> u.getPassword().equals(request.getPassword()))
                .map(u -> ResponseEntity.ok("Login successful"))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid credentials"));
    }

}
