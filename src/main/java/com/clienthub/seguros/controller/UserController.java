package com.clienthub.seguros.controller;

import com.clienthub.seguros.model.Role;
import com.clienthub.seguros.model.User;
import com.clienthub.seguros.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Modulo solo visible/accesible para el ADMIN (restringido tambien en SecurityConfig).
 * Aqui es donde el admin da de alta las credenciales de los agentes.
 */
@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("users", userService.findAll());
        return "users/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("userForm", new UserForm());
        model.addAttribute("roles", Role.values());
        return "users/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute UserForm userForm) {
        User user = new User();
        user.setFullName(userForm.getFullName());
        user.setUsername(userForm.getUsername());
        user.setRole(userForm.getRole());
        userService.create(user, userForm.getRawPassword());
        return "redirect:/users";
    }

    @PostMapping("/{id}/toggle-active")
    public String toggleActive(@PathVariable Long id) {
        userService.toggleActive(id);
        return "redirect:/users";
    }
}
