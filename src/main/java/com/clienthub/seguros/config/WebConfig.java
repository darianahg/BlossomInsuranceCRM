package com.clienthub.seguros.config;

import com.clienthub.seguros.model.Client;
import com.clienthub.seguros.model.User;
import com.clienthub.seguros.repository.ClientRepository;
import com.clienthub.seguros.repository.UserRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Los formularios HTML solo pueden enviar texto (el id como String), pero las
 * entidades Client/Contract necesitan el objeto completo (User, Client).
 * Estos "conversores" le enseñan a Spring como pasar de un id de texto al
 * objeto real de la base de datos. Sin esto, los formularios de clientes y
 * contratos fallarian al guardar.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final UserRepository userRepository;
    private final ClientRepository clientRepository;

    public WebConfig(UserRepository userRepository, ClientRepository clientRepository) {
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new Converter<String, User>() {
            @Override
            public User convert(String id) {
                if (id == null || id.isBlank()) return null;
                return userRepository.findById(Long.valueOf(id)).orElse(null);
            }
        });

        registry.addConverter(new Converter<String, Client>() {
            @Override
            public Client convert(String id) {
                if (id == null || id.isBlank()) return null;
                return clientRepository.findById(Long.valueOf(id)).orElse(null);
            }
        });
    }
}
