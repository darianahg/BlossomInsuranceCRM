package com.clienthub.seguros.repository;

import com.clienthub.seguros.model.Contract;
import com.clienthub.seguros.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface ContractRepository extends JpaRepository<Contract, Long> {

    // Contratos de los clientes asignados a un agente especifico.
    List<Contract> findByClient_AssignedAgent(User assignedAgent);

    // Contratos que vencen entre hoy y una fecha limite (para alertas/dashboard).
    List<Contract> findByEndDateBetween(LocalDate start, LocalDate end);

    List<Contract> findByClient_AssignedAgentAndEndDateBetween(User assignedAgent, LocalDate start, LocalDate end);
}
