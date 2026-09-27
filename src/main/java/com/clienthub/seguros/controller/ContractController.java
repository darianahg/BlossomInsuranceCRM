package com.clienthub.seguros.controller;

import com.clienthub.seguros.model.Contract;
import com.clienthub.seguros.model.ContractStatus;
import com.clienthub.seguros.model.InsuranceType;
import com.clienthub.seguros.service.ClientService;
import com.clienthub.seguros.service.ContractService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/contracts")
public class ContractController {

    private final ContractService contractService;
    private final ClientService clientService;

    public ContractController(ContractService contractService, ClientService clientService) {
        this.contractService = contractService;
        this.clientService = clientService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("contracts", contractService.findVisibleContracts());
        return "contracts/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("contract", new Contract());
        addFormAttributes(model);
        return "contracts/form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("contract", contractService.findById(id));
        addFormAttributes(model);
        return "contracts/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("contract") Contract contract, BindingResult result, Model model) {
        if (result.hasErrors()) {
            addFormAttributes(model);
            return "contracts/form";
        }
        contractService.save(contract);
        return "redirect:/contracts";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        contractService.delete(id);
        return "redirect:/contracts";
    }

    private void addFormAttributes(Model model) {
        model.addAttribute("clients", clientService.findVisibleClients());
        model.addAttribute("insuranceTypes", InsuranceType.values());
        model.addAttribute("statuses", ContractStatus.values());
    }

    /** Reporte exportable: descarga los contratos visibles como CSV (se abre directo en Excel). */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=contratos.csv");

        response.getOutputStream().write(0xEF);
        response.getOutputStream().write(0xBB);
        response.getOutputStream().write(0xBF);

        PrintWriter writer = new PrintWriter(response.getOutputStream(), true, StandardCharsets.UTF_8);
        writer.println("Poliza,Cliente,Tipo,Aseguradora,Prima,Fecha inicio,Fecha vencimiento,Estado");
        for (Contract c : contractService.findVisibleContracts()) {
            writer.printf("%s,%s,%s,%s,%s,%s,%s,%s%n",
                    csv(c.getPolicyNumber()), csv(c.getClient().getFullName()), csv(c.getInsuranceType().name()),
                    csv(c.getInsurer()), c.getPremium(), c.getStartDate(), c.getEndDate(), csv(c.getStatus().name()));
        }
        writer.flush();
    }

    private String csv(String value) {
        if (value == null) return "";
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
}
