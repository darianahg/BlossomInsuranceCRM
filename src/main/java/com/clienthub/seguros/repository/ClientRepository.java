package com.clienthub.seguros.repository;

import com.clienthub.seguros.model.Client;
import com.clienthub.seguros.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Long> {

    // Usado para que un agente solo vea sus propios clientes.
    List<Client> findByAssignedAgent(User assignedAgent);

    List<Client> findByFullNameContainingIgnoreCase(String fullName);
}
