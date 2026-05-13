package com.ejemplo.clienteservice.domain.services;

import com.ejemplo.clienteservice.domain.exception.ClienteNotFoundException;
import com.ejemplo.clienteservice.domain.exception.ClienteServiceException;
import com.ejemplo.clienteservice.domain.entities.Cliente;
import com.ejemplo.clienteservice.domain.entities.Region;
import com.ejemplo.clienteservice.domain.repositories.IClienteDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClienteServiceImpl implements IClienteService {

    @Autowired
    private IClienteDao clienteDao;

    // ==============================
    // ✅ LISTAR PAGINADO
    // ==============================
    @Override
    @Transactional(readOnly = true)
    public Page<Cliente> findAll(Pageable pageable) {
        return clienteDao.findAll(pageable);
    }

    // ==============================
    // ✅ LISTAR TODOS
    // ==============================
    @Override
    @Transactional(readOnly = true)
    public List<Cliente> findAll() {
        return clienteDao.findAll();
    }

    // ==============================
    // ✅ BUSCAR POR ID
    // ==============================
    @Override
    @Transactional(readOnly = true)
    public Cliente findById(Long id) {

        return clienteDao.findById(id)
                .orElseThrow(() ->
                        new ClienteNotFoundException(id));
    }

    // ==============================
    // ✅ GUARDAR
    // ==============================
    @Override
    @Transactional
    public Cliente save(Cliente cliente) {

        try {
            return clienteDao.save(cliente);

        } catch (DataIntegrityViolationException ex) {

            throw new ClienteServiceException(
                    "No se pudo guardar el cliente: datos duplicados",
                    ex
            );
        }
    }

    // ==============================
    // ✅ ACTUALIZAR
    // ==============================
    @Override
    @Transactional
    public Cliente update(Long id, Cliente cliente) {

        Cliente actual = clienteDao.findById(id)
                .orElseThrow(() ->
                        new ClienteNotFoundException(id));

        actual.setNombre(cliente.getNombre());
        actual.setApellido(cliente.getApellido());
        actual.setEmail(cliente.getEmail());
        actual.setFoto(cliente.getFoto());
        actual.setRegion(cliente.getRegion());

        return clienteDao.save(actual);
    }

    // ==============================
    // ✅ ELIMINAR
    // ==============================
    @Override
    @Transactional
    public void delete(Long id) {

        if (!clienteDao.existsById(id)) {
            throw new ClienteNotFoundException(id);
        }

        clienteDao.deleteById(id);
    }

    // ==============================
    // ✅ REGIONES
    // ==============================
    @Override
    @Transactional(readOnly = true)
    public List<Region> findAllRegiones() {
        return clienteDao.findAllRegiones();
    }
}