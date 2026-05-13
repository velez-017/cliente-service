package com.ejemplo.clienteservice.domain.services;

import com.ejemplo.clienteservice.domain.entities.Cliente;
import com.ejemplo.clienteservice.domain.entities.Region;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IClienteService {

    /**
     * Lista clientes paginados
     */
    Page<Cliente> findAll(Pageable pageable);

    /**
     * Lista todos los clientes
     */
    List<Cliente> findAll();

    /**
     * Busca un cliente por ID
     */
    Cliente findById(Long id);

    /**
     * Guarda un nuevo cliente
     */
    Cliente save(Cliente cliente);

    /**
     * Actualiza cliente preservando createAt
     */
    Cliente update(Long id, Cliente cliente);

    /**
     * Elimina cliente
     */
    void delete(Long id);

    /**
     * Lista regiones
     */
    List<Region> findAllRegiones();
}