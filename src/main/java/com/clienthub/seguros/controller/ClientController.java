package com.clienthub.seguros.controller;

import com.clienthub.seguros.model.Client;
import com.clienthub.seguros.model.Role;
import com.clienthub.seguros.repository.UserRepository;
import com.clienthub.seguros.service.ClientService;
import com.clienthub.seguros.service.CurrentUserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;
    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;

    public ClientController(ClientService clientService, UserRepository userRepository,
                             CurrentUserService currentUserService) {
        this.clientService = clientService;
        this.userRepository = userRepository;
        this.currentUserService = currentUserService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("clients", clientService.findVisibleClients());
        model.addAttribute("isAdmin", currentUserService.isAdmin());
        return "clients/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("client", new Client());
        model.addAttribute("agents", userRepository.findByRole(Role.AGENT));
        model.addAttribute("isAdmin", currentUserService.isAdmin());
        return "clients/form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("client", clientService.findById(id));
        model.addAttribute("agents", userRepository.findByRole(Role.AGENT));
        model.addAttribute("isAdmin", currentUserService.isAdmin());
        return "clients/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("client") Client client, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("agents", userRepository.findByRole(Role.AGENT));
            return "clients/form";
        }

        // Si el que guarda es un agente, el cliente queda asignado a el mismo automaticamente.
        if (!currentUserService.isAdmin()) {
            client.setAssignedAgent(currentUserService.getCurrentUser());
        }

        clientService.save(client);
        return "redirect:/clients";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        clientService.delete(id);
        return "redirect:/clients";
    }

    /** Reporte exportable: descarga los clientes visibles como CSV (se abre directo en Excel). */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=clientes.csv");

        // BOM para que Excel muestre bien las tildes/eñes.
        response.getOutputStream().write(0xEF);
        response.getOutputStream().write(0xBB);
        response.getOutputStream().write(0xBF);

        PrintWriter writer = new PrintWriter(response.getOutputStream(), true, StandardCharsets.UTF_8);
        writer.println("Nombre,Email,Telefono,Ciudad,Agente asignado");
        for (Client c : clientService.findVisibleClients()) {
            String agente = c.getAssignedAgent() != null ? c.getAssignedAgent().getFullName() : "Sin asignar";
            writer.printf("%s,%s,%s,%s,%s%n",
                    csv(c.getFullName()), csv(c.getEmail()), csv(c.getPhone()), csv(c.getCity()), csv(agente));
        }
        writer.flush();
    }

    private String csv(String value) {
        if (value == null) return "";
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
}
