package com.clienthub.seguros.model;

/**
 * Rol del usuario dentro del sistema.
 * ADMIN ve todo. AGENT solo ve sus propios clientes y contratos.
 */
public enum Role {
    ADMIN,
    AGENT
}
