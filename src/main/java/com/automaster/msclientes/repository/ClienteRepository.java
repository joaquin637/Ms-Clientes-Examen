package com.automaster.msclientes.repository;

import com.automaster.msclientes.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    boolean existsByRut(String rut);
    boolean existsByEmail(String email);
    Optional<Cliente> findByRut(String rut);
}