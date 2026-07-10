package com.automaster.msclientes.service;

import com.automaster.msclientes.dto.ClienteRequestDTO;
import com.automaster.msclientes.dto.ClienteResponseDTO;
import com.automaster.msclientes.model.Cliente;
import com.automaster.msclientes.repository.ClienteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override

    public ClienteResponseDTO registrarCliente(ClienteRequestDTO request) {
        log.info("Iniciando registro de nuevo cliente con RUT: {}", request.getRut());

        if (clienteRepository.existsByRut(request.getRut())) {
            log.error("Rechazado: El RUT {} ya está registrado en el sistema", request.getRut());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El RUT ingresado ya se encuentra registrado.");
        }
        if (clienteRepository.existsByEmail(request.getEmail())) {
            log.error("Rechazado: El Email {} ya está en uso", request.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo electrónico ya está asociado a otro cliente.");
        }
        Cliente cliente = new Cliente();
        cliente.setRut(request.getRut());
        cliente.setNombre(request.getNombre());
        cliente.setApellido(request.getApellido());
        cliente.setEmail(request.getEmail());
        cliente.setTelefono(request.getTelefono());

        Cliente guardado = clienteRepository.save(cliente);
        log.info("Cliente registrado con éxito. ID asignado: {}", guardado.getId());

        return mapearADTO(guardado);
    }

    @Override
    public ClienteResponseDTO buscarPorRut(String rut) {
        log.info("Consultando base de datos por cliente con RUT: {}", rut);

        Cliente cliente = clienteRepository.findByRut(rut)
                .orElseThrow(() -> {
                    log.error("Búsqueda fallida: No se encontró cliente con RUT {}", rut);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "El cliente no existe en los registros.");
                });
        return mapearADTO(cliente);
    }

    private ClienteResponseDTO mapearADTO(Cliente cliente) {
        ClienteResponseDTO dto = new ClienteResponseDTO();
        dto.setId(cliente.getId());
        dto.setRut(cliente.getRut());
        dto.setNombre(cliente.getNombre());
        dto.setApellido(cliente.getApellido());
        dto.setEmail(cliente.getEmail());
        dto.setTelefono(cliente.getTelefono());
        return dto;
    }

    @Override
    public ClienteResponseDTO buscarPorId(Long id) {
        log.info("Consultando base de datos por cliente con id: {}", id);
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Búsqueda fallida: No se encontró cliente con id {}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "El cliente no existe en los registros.");
                });
        return mapearADTO(cliente);
    }

    public List<ClienteResponseDTO> listarTodos() {
        log.info("Buscando todos los clientes en la base de datos");
        List<Cliente> clientes = clienteRepository.findAll();
        return clientes.stream()
                .map(this::mapearADTO)
                .collect(Collectors.toList());
    }

    public void eliminarCliente(Long id) {
        log.info("Iniciando eliminación de cliente con ID: {}", id);
        if (!clienteRepository.existsById(id)) {
            log.error("Error al eliminar: Cliente ID {} no encontrado", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se puede eliminar porque el cliente no existe.");
        }
        clienteRepository.deleteById(id);
        log.info("Cliente ID {} eliminado con éxito", id);
    }
}