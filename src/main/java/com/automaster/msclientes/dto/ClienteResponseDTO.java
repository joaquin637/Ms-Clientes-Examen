package com.automaster.msclientes.dto;

import lombok.Data;

@Data
public class ClienteResponseDTO  {
    private Long id;
    private String rut;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
}