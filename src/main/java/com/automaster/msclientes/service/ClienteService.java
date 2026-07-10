package com.automaster.msclientes.service;

import com.automaster.msclientes.dto.ClienteRequestDTO;
import com.automaster.msclientes.dto.ClienteResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ClienteService {
    ClienteResponseDTO registrarCliente(ClienteRequestDTO request);
    ClienteResponseDTO buscarPorRut(String rut);
    ClienteResponseDTO buscarPorId(Long id);
    List<ClienteResponseDTO> listarTodos();
    void eliminarCliente(Long id);
}
