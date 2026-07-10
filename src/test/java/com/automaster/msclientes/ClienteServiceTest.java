package com.automaster.msclientes;

import com.automaster.msclientes.dto.ClienteRequestDTO;
import com.automaster.msclientes.dto.ClienteResponseDTO;
import com.automaster.msclientes.model.Cliente;
import com.automaster.msclientes.repository.ClienteRepository;
import com.automaster.msclientes.service.ClienteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    private Cliente clienteMock;
    private ClienteRequestDTO clienteRequestMock;

    @BeforeEach
    void setUp() {
        clienteMock = new Cliente();
        clienteMock.setId(1L);
        clienteMock.setRut("12345678-9");
        clienteMock.setNombre("Juan");
        clienteMock.setApellido("Pérez");
        clienteMock.setEmail("juan.perez@email.com");
        clienteMock.setTelefono("+56912345678");

        clienteRequestMock = new ClienteRequestDTO();
        clienteRequestMock.setRut("12345678-9");
        clienteRequestMock.setNombre("Juan");
        clienteRequestMock.setApellido("Pérez");
        clienteRequestMock.setEmail("juan.perez@email.com");
        clienteRequestMock.setTelefono("+56912345678");
    }

    @Test
    void testListarTodos() {
        when(clienteRepository.findAll()).thenReturn(List.of(clienteMock));

        List<ClienteResponseDTO> resultado = clienteService.listarTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Juan", resultado.get(0).getNombre());
        verify(clienteRepository, times(1)).findAll();
    }

    @Test
    void testBuscarPorIdExitoso() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteMock));

        ClienteResponseDTO resultado = clienteService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals("12345678-9", resultado.getRut());
        assertEquals("Juan", resultado.getNombre());
        verify(clienteRepository, times(1)).findById(1L);
    }

    @Test
    void testBuscarPorIdNoEncontrado() {
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> clienteService.buscarPorId(99L));
        verify(clienteRepository, times(1)).findById(99L);
    }

    @Test
    void testGuardarCliente() {
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteMock);

        ClienteResponseDTO resultado = clienteService.registrarCliente(clienteRequestMock);

        assertNotNull(resultado);
        assertEquals("12345678-9", resultado.getRut());
        assertEquals("Juan", resultado.getNombre());
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    void testEliminarClienteExitoso() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteMock));
        doNothing().when(clienteRepository).deleteById(1L);

        assertDoesNotThrow(() -> clienteService.eliminarCliente(1L));
        verify(clienteRepository, times(1)).findById(1L);
        verify(clienteRepository, times(1)).deleteById(1L);
    }

    @Test
    void testEliminarClienteNoEncontrado() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> clienteService.eliminarCliente(1L));
        verify(clienteRepository, times(1)).findById(1L);
        verify(clienteRepository, never()).deleteById(anyLong());
    }
}