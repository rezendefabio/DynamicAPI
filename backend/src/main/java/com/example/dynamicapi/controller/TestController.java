package com.example.dynamicapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/security")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> testSecurity() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Usuário autenticado: " + authentication.getName());
        System.out.println("Autorizações: " + authentication.getAuthorities());
        return ResponseEntity.ok("Acesso permitido!");
    }
}