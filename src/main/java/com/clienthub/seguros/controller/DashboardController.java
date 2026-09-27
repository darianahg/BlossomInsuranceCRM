package com.clienthub.seguros.controller;

import com.clienthub.seguros.service.ClientService;
import com.clienthub.seguros.service.ContractService;
import com.clienthub.seguros.service.CurrentUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final ClientService clientService;
    private final ContractService contractService;
    private final CurrentUserService currentUserService;

    public DashboardController(ClientService clientService, ContractService contractService,
                                CurrentUserService currentUserService) {
        this.clientService = clientService;
        this.contractService = contractService;
        this.currentUserService = currentUserService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("currentUser", currentUserService.getCurrentUser());
        model.addAttribute("totalClients", clientService.countVisibleClients());
        model.addAttribute("totalContracts", contractService.countVisibleContracts());
        model.addAttribute("activeContracts", contractService.countActive());
        model.addAttribute("expiringSoon", contractService.findExpiringSoon());
        return "dashboard";
    }
}
