package com.example.dynamicapi.service;

import com.example.dynamicapi.exception.CustomException;
import com.example.dynamicapi.model.User;
import com.example.dynamicapi.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String authenticate(String username, String password) {
        // Buscar o usuário no banco de dados pelo nome de usuário
        Optional<User> userOptional = userRepository.findByUsername(username);

        // Verificar se o usuário existe e se a senha está correta
        if (userOptional.isEmpty() || !passwordEncoder.matches(password, userOptional.get().getPassword())) {
            throw new CustomException("Invalid username or password");
        }

        // Retornar a role do usuário
        return userOptional.get().getRole();
    }
}