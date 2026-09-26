package com.clienthub.seguros.service;

import com.clienthub.seguros.model.Client;
import com.clienthub.seguros.model.User;
import com.clienthub.seguros.repository.ClientRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final CurrentUserService currentUserService;

    public ClientService(ClientRepository clientRepository, CurrentUserService currentUserService) {
        this.clientRepository = clientRepository;
        this.currentUserService = currentUserService;
    }

    /** Admin ve todos los clientes; agente solo ve los suyos. */
    public List<Client> findVisibleClients() {
        User current = currentUserService.getCurrentUser();
        if (currentUserService.isAdmin()) {
            return clientRepository.findAll();
        }
        return clientRepository.findByAssignedAgent(current);
    }

    public Client findById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
    }

    public Client save(Client client) {
        return clientRepository.save(client);
    }

    public void delete(Long id) {
        clientRepository.deleteById(id);
    }

    public long countVisibleClients() {
        return findVisibleClients().size();
    }
}
