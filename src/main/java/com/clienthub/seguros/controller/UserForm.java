package com.clienthub.seguros.controller;

import com.clienthub.seguros.model.Role;

/** Datos que vienen del formulario para crear un usuario nuevo (admin o agente). */
public class UserForm {

    private String fullName;
    private String username;
    private String rawPassword;
    private Role role = Role.AGENT;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRawPassword() {
        return rawPassword;
    }

    public void setRawPassword(String rawPassword) {
        this.rawPassword = rawPassword;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
