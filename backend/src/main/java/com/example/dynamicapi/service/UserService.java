package com.example.dynamicapi.service;

import com.example.dynamicapi.exception.CustomException;
import com.example.dynamicapi.model.User;
import com.example.dynamicapi.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Criar um novo usuário
    public User createUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new CustomException("Username already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // Listar todos os usuários
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Obter detalhes de um usuário específico
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new CustomException("User not found with id: " + id));
    }

    // Atualizar um usuário existente
    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException("User not found with id: " + id));

        user.setUsername(userDetails.getUsername());
        user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        user.setRole(userDetails.getRole());

        return userRepository.save(user);
    }

    // Excluir um usuário
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}