package com.clienthub.seguros.service;

import com.clienthub.seguros.model.Contract;
import com.clienthub.seguros.model.User;
import com.clienthub.seguros.repository.ContractRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class ContractService {

    // Un contrato se considera "por vencer" dentro de esta cantidad de dias.
    private static final int DIAS_ALERTA_VENCIMIENTO = 30;

    private final ContractRepository contractRepository;
    private final CurrentUserService currentUserService;

    public ContractService(ContractRepository contractRepository, CurrentUserService currentUserService) {
        this.contractRepository = contractRepository;
        this.currentUserService = currentUserService;
    }

    /** Admin ve todos los contratos; agente solo los de sus clientes. */
    public List<Contract> findVisibleContracts() {
        User current = currentUserService.getCurrentUser();
        if (currentUserService.isAdmin()) {
            return contractRepository.findAll();
        }
        return contractRepository.findByClient_AssignedAgent(current);
    }

    public List<Contract> findExpiringSoon() {
        User current = currentUserService.getCurrentUser();
        LocalDate hoy = LocalDate.now();
        LocalDate limite = hoy.plusDays(DIAS_ALERTA_VENCIMIENTO);

        if (currentUserService.isAdmin()) {
            return contractRepository.findByEndDateBetween(hoy, limite);
        }
        return contractRepository.findByClient_AssignedAgentAndEndDateBetween(current, hoy, limite);
    }

    public Contract findById(Long id) {
        return contractRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Contrato no encontrado"));
    }

    public Contract save(Contract contract) {
        return contractRepository.save(contract);
    }

    public void delete(Long id) {
        contractRepository.deleteById(id);
    }

    public long countVisibleContracts() {
        return findVisibleContracts().size();
    }

    public long countActive() {
        return findVisibleContracts().stream()
                .filter(c -> c.getStatus().name().equals("ACTIVO"))
                .count();
    }
}
