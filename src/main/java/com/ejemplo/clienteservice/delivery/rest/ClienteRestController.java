package com.ejemplo.clienteservice.delivery.rest;

import com.ejemplo.clienteservice.domain.entities.Cliente;
import com.ejemplo.clienteservice.domain.entities.Region;
import com.ejemplo.clienteservice.domain.services.IClienteService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/cliente-service")
@CrossOrigin(origins = {"http://localhost:4200"})
public class ClienteRestController {

    @Autowired
    private IClienteService clienteService;

    // ==============================
    // ✅ LISTAR SIN PAGINACIÓN
    // ==============================
    @GetMapping("/clientes")
    public ResponseEntity<List<Cliente>> listarClientes() {
        return ResponseEntity.ok(clienteService.findAll());
    }

    // ==============================
    // ✅ LISTAR CON PAGINACIÓN
    // ==============================
    @GetMapping("/clientes/page/{page}")
    public ResponseEntity<Page<Cliente>> listarClientesPaginado(
            @PathVariable Integer page) {

        return ResponseEntity.ok(
                clienteService.findAll(PageRequest.of(page, 4))
        );
    }

    // ==============================
    // ✅ BUSCAR POR ID
    // ==============================
    @GetMapping("/clientes/{id}")
    public ResponseEntity<Cliente> buscarCliente(@PathVariable Long id) {

        return ResponseEntity.ok(
                clienteService.findById(id)
        );
    }

    // ==============================
    // ✅ CREAR
    // ==============================
    @PostMapping("/clientes")
    public ResponseEntity<?> crearCliente(
            @Valid @RequestBody Cliente cliente,
            BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity
                    .badRequest()
                    .body(obtenerErrores(result));
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(clienteService.save(cliente));
    }

    // ==============================
    // ✅ ACTUALIZAR
    // ==============================
    @PutMapping("/clientes/{id}")
    public ResponseEntity<?> actualizarCliente(
            @Valid @RequestBody Cliente cliente,
            BindingResult result,
            @PathVariable Long id) {

        if (result.hasErrors()) {
            return ResponseEntity
                    .badRequest()
                    .body(obtenerErrores(result));
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(clienteService.update(id, cliente));
    }

    // ==============================
    // ✅ ELIMINAR
    // ==============================
    @DeleteMapping("/clientes/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Long id) {

        clienteService.delete(id);

        return ResponseEntity
                .noContent()
                .build();
    }

    // ==============================
    // ✅ REGIONES
    // ==============================
    @GetMapping("/clientes/regiones")
    public ResponseEntity<List<Region>> listarRegiones() {

        return ResponseEntity.ok(
                clienteService.findAllRegiones()
        );
    }

    // ==============================
    // 🔧 MÉTODO AUXILIAR
    // ==============================
    private Map<String, Object> obtenerErrores(BindingResult result) {

        Map<String, Object> errores = new HashMap<>();

        List<String> listaErrores = result
                .getFieldErrors()
                .stream()
                .map(e -> "El campo '" + e.getField() + "' " + e.getDefaultMessage())
                .toList();

        errores.put("errors", listaErrores);

        return errores;
    }
}