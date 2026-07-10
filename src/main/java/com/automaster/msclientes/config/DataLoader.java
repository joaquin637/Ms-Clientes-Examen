package com.automaster.msclientes.config;

import com.automaster.msclientes.model.Cliente;
import com.automaster.msclientes.repository.ClienteRepository;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


@Slf4j
@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();

        log.info("Generando datos falsos para Mantencion");

        for (int i = 0; i < 30; i++) {
            Cliente cliente = new Cliente();

            cliente.setRut(faker.regexify("[0-9]{11}[0-9kK]"));
            cliente.setNombre(faker.name().firstName());
            cliente.setApellido(faker.name().lastName());
            cliente.setEmail(faker.internet().emailAddress());
            cliente.setTelefono(faker.number().digits(9));

            clienteRepository.save(cliente);
        }

        log.info("Datos de prueba de mantenciones generados con éxito");
    }
}
