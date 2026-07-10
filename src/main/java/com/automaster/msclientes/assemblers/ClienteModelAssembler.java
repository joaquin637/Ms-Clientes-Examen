package com.automaster.msclientes.assemblers;

import com.automaster.msclientes.controller.ClienteControllerV2;
import com.automaster.msclientes.dto.ClienteResponseDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ClienteModelAssembler implements RepresentationModelAssembler<ClienteResponseDTO, EntityModel<ClienteResponseDTO>> {

    @Override
    public EntityModel<ClienteResponseDTO> toModel(ClienteResponseDTO cliente) {
        return EntityModel.of(cliente,
                linkTo(methodOn(ClienteControllerV2.class).obtenerClientePorId(cliente.getId())).withSelfRel(),
                linkTo(methodOn(ClienteControllerV2.class).listarClientes()).withRel("clientes"));
    }
}
