package com.automaster.msclientes.controller;

import com.automaster.msclientes.assemblers.ClienteModelAssembler;
import com.automaster.msclientes.dto.ClienteResponseDTO;
import com.automaster.msclientes.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequestMapping("/api/v2/clientes")
@Tag(name= "Clientes", description = "Operaciones relacionadas a Clientes con HATEOAS")
public class ClienteControllerV2 {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ClienteModelAssembler assembler;

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Buscar Clientes con HATEOAS", description = "Busca clientes por Id (HATEOAS)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado exitosamente con enlaces HATEOAS",
                    content = @Content(mediaType = "application/hal+json",
                            schema = @Schema(implementation = ClienteResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado con el ID proporcionado")
    })
    public EntityModel<ClienteResponseDTO> obtenerClientePorId(@PathVariable Long id) {
        ClienteResponseDTO cliente = clienteService.buscarPorId(id);
        return assembler.toModel(cliente);
    }

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Listar Clientes con HATEOAS", description = "Muestra una lista con todos los clientes (HATEOAS)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de clientes obtenida exitosamente con enlaces HATEOAS",
                    content = @Content(mediaType = "application/hal+json",
                            array = @ArraySchema(schema = @Schema(implementation = ClienteResponseDTO.class)))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al obtener los clientes")
    })
    public CollectionModel<EntityModel<ClienteResponseDTO>> listarClientes() {
        List<EntityModel<ClienteResponseDTO>> clientes = clienteService.listarTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(clientes,
                linkTo(methodOn(ClienteControllerV2.class).listarClientes()).withSelfRel());
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Eliminar Cliente por Id con HATEOAS", description = "Elimina cualquier cliente por su Id (HATEOAS)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente eliminado exitosamente, devuelve los datos del cliente con enlaces HATEOAS",
                    content = @Content(mediaType = "application/hal+json",
                            schema = @Schema(implementation = ClienteResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado con el ID proporcionado")
    })
    public EntityModel<ClienteResponseDTO> eliminarCliente(@PathVariable Long id) {
        log.info("Petición DELETE recibida para eliminar cliente ID: {}", id);
        ClienteResponseDTO cliente = clienteService.buscarPorId(id);
        clienteService.eliminarCliente(id);
        return assembler.toModel(cliente);
    }

}