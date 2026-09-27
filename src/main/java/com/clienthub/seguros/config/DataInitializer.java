package com.clienthub.seguros.config;

import com.clienthub.seguros.model.*;
import com.clienthub.seguros.repository.ClientRepository;
import com.clienthub.seguros.repository.ContractRepository;
import com.clienthub.seguros.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Crea datos de ejemplo la primera vez que se arranca la aplicacion,
 * para poder hacer login y ver el CRM funcionando sin capturar nada a mano.
 *
 * Usuarios de prueba:
 *   admin    / admin123   (rol ADMIN, ve todo)
 *   agente1  / agente123  (rol AGENT, solo ve sus clientes)
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final ContractRepository contractRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, ClientRepository clientRepository,
                            ContractRepository contractRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.contractRepository = contractRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return; // Ya hay datos, no se vuelve a sembrar.
        }

        User admin = new User("Administrador General", "admin",
                passwordEncoder.encode("admin123"), Role.ADMIN);
        userRepository.save(admin);

        User agente = new User("Carlos Pérez", "agente1",
                passwordEncoder.encode("agente123"), Role.AGENT);
        userRepository.save(agente);

        Client c1 = new Client();
        c1.setFullName("María López");
        c1.setEmail("maria.lopez@example.com");
        c1.setPhone("9999-1111");
        c1.setCity("San Pedro Sula");
        c1.setAssignedAgent(agente);
        clientRepository.save(c1);

        Client c2 = new Client();
        c2.setFullName("José Martínez");
        c2.setEmail("jose.martinez@example.com");
        c2.setPhone("9999-2222");
        c2.setCity("Tegucigalpa");
        c2.setAssignedAgent(agente);
        clientRepository.save(c2);

        Client c3 = new Client();
        c3.setFullName("Empresa Constructora del Norte S.A.");
        c3.setEmail("contacto@constructoradelnorte.com");
        c3.setPhone("2552-3344");
        c3.setCity("San Pedro Sula");
        c3.setAssignedAgent(null); // Cliente manejado directo por el admin
        clientRepository.save(c3);

        Contract ct1 = new Contract();
        ct1.setPolicyNumber("POL-VIDA-0001");
        ct1.setInsuranceType(InsuranceType.VIDA);
        ct1.setInsurer("Seguros del Istmo");
        ct1.setPremium(new BigDecimal("450.00"));
        ct1.setStartDate(LocalDate.now().minusMonths(6));
        ct1.setEndDate(LocalDate.now().plusDays(15)); // por vencer pronto, para probar alertas
        ct1.setStatus(ContractStatus.POR_VENCER);
        ct1.setClient(c1);
        contractRepository.save(ct1);

        Contract ct2 = new Contract();
        ct2.setPolicyNumber("POL-AUTO-0002");
        ct2.setInsuranceType(InsuranceType.AUTO);
        ct2.setInsurer("Aseguradora General");
        ct2.setPremium(new BigDecimal("780.50"));
        ct2.setStartDate(LocalDate.now().minusMonths(2));
        ct2.setEndDate(LocalDate.now().plusMonths(10));
        ct2.setStatus(ContractStatus.ACTIVO);
        ct2.setClient(c2);
        contractRepository.save(ct2);

        Contract ct3 = new Contract();
        ct3.setPolicyNumber("POL-HOGAR-0003");
        ct3.setInsuranceType(InsuranceType.HOGAR);
        ct3.setInsurer("Seguros del Istmo");
        ct3.setPremium(new BigDecimal("320.00"));
        ct3.setStartDate(LocalDate.now().minusYears(1));
        ct3.setEndDate(LocalDate.now().minusDays(5)); // ya vencido, para probar el estado
        ct3.setStatus(ContractStatus.VENCIDO);
        ct3.setClient(c3);
        contractRepository.save(ct3);
    }
}
