package com.automaster.msclientes.controller;

import com.automaster.msclientes.dto.ClienteRequestDTO;
import com.automaster.msclientes.dto.ClienteResponseDTO;
import com.automaster.msclientes.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/clientes")
@Tag(name= "Clientes", description = "Operaciones relacionadas a Clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    @Operation(summary = "Listar Clientes", description = "Muestra una lista con todos los clientes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de clientes obtenida exitosamente",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ClienteResponseDTO.class)))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al obtener los clientes")
    })
    public ResponseEntity<List<ClienteResponseDTO>> listarClientes() {
        log.info("Petición GET recibida para listar todos los clientes");
        List<ClienteResponseDTO> response = clienteService.listarTodos();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PostMapping
    @Operation(summary = "Agregar Clientes", description = "Agregas nuevos clientes para la base de datos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente creado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClienteResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos de petición inválidos o incompletos")
    })
    public ResponseEntity<ClienteResponseDTO> crearCliente(@Valid @RequestBody ClienteRequestDTO request) {
        log.info("Petición REST POST entrante para crear cliente");
        ClienteResponseDTO response = clienteService.registrarCliente(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/rut/{rut}")
    @Operation(summary = "Buscar Cliente por Rut", description = "Encuentra cualquier cliente por su Rut")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClienteResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado con el RUT proporcionado")
    })
    public ResponseEntity<ClienteResponseDTO> obtenerClientePorRut(@PathVariable String rut) {
        log.info("Petición REST GET entrante para buscar cliente por RUT");
        ClienteResponseDTO response = clienteService.buscarPorRut(rut);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar Cliente por Id", description = "Encuentra clientes por su Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClienteResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado con el ID proporcionado")
    })
    public ResponseEntity<ClienteResponseDTO> obtenerPorId(@PathVariable Long id) {
        ClienteResponseDTO response = clienteService.buscarPorId(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar Cliente por Id", description = "Elimina cualquier cliente por su Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cliente eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado con el ID proporcionado")
    })
    public ResponseEntity<Void> eliminarCliente(@PathVariable Long id) {
        log.info("Petición DELETE recibida para eliminar cliente ID: {}", id);
        clienteService.eliminarCliente(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}