package com.clienthub.seguros.service;

import com.clienthub.seguros.model.Role;
import com.clienthub.seguros.model.User;
import com.clienthub.seguros.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Pequeña utilidad para saber, en cualquier controlador,
 * quien esta logueado y si es admin o agente.
 */
@Service
public class CurrentUserService {

    private final UserRepository userRepository;

    public CurrentUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("Usuario logueado no existe en la base de datos"));
    }

    public boolean isAdmin() {
        return getCurrentUser().getRole() == Role.ADMIN;
    }
}
