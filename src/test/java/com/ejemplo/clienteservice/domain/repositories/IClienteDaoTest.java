package com.ejemplo.clienteservice.domain.repositories;

import com.ejemplo.clienteservice.domain.entities.Cliente;
import com.ejemplo.clienteservice.domain.entities.Region;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@DisplayName("Integration tests (data slice) — IClienteDao con H2")
class IClienteDaoTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private IClienteDao clienteDao;

    private Region region;
    private Cliente cliente;

    @BeforeEach
    void setUp() {

        // ===== REGION =====
        region = new Region();
        region.setNombre("Asia");

        em.persist(region);

        // ===== CLIENTE =====
        cliente = new Cliente();
        cliente.setNombre("Grace");
        cliente.setApellido("Hopper");
        cliente.setEmail("grace@navy.mil");
        cliente.setRegion(region);

        em.persist(cliente);

        // fuerza INSERT en H2
        em.flush();
    }

    // =====================================================
    // FIND ALL
    // =====================================================

    @Test
    @DisplayName("findAll — debe retornar clientes persistidos")
    void findAll_debeRetornarClientes() {

        List<Cliente> clientes = clienteDao.findAll();

        assertThat(clientes).hasSize(1);
        assertThat(clientes.get(0).getNombre())
                .isEqualTo("Grace");
    }

    // =====================================================
    // FIND BY ID
    // =====================================================

    @Test
    @DisplayName("findById — ID existente → retorna cliente")
    void findById_idExistente_debeRetornarCliente() {

        Optional<Cliente> encontrado =
                clienteDao.findById(cliente.getId());

        assertThat(encontrado).isPresent();

        assertThat(encontrado.get().getEmail())
                .isEqualTo("grace@navy.mil");
    }

    @Test
    @DisplayName("findById — ID inexistente → Optional vacío")
    void findById_idInexistente_debeRetornarVacio() {

        Optional<Cliente> encontrado =
                clienteDao.findById(999L);

        assertThat(encontrado).isEmpty();
    }

    // =====================================================
    // SAVE
    // =====================================================

    @Test
    @DisplayName("save — debe persistir nuevo cliente")
    void save_debePersistirCliente() {

        Cliente nuevo = new Cliente();
        nuevo.setNombre("Alan");
        nuevo.setApellido("Turing");
        nuevo.setEmail("alan@crypto.uk");
        nuevo.setRegion(region);

        Cliente guardado = clienteDao.save(nuevo);

        assertThat(guardado.getId()).isNotNull();
        assertThat(guardado.getNombre())
                .isEqualTo("Alan");
    }

    // =====================================================
    // DELETE
    // =====================================================

    @Test
    @DisplayName("deleteById — elimina el cliente")
    void deleteById_debeEliminarCliente() {

        Long id = cliente.getId();

        clienteDao.deleteById(id);

        em.flush();

        Cliente eliminado = em.find(Cliente.class, id);

        assertThat(eliminado).isNull();
    }

    // =====================================================
    // PAGINACION
    // =====================================================

    @Test
    @DisplayName("findAll pageable — debe retornar página")
    void findAllPageable_debeRetornarPagina() {

        Page<Cliente> pagina =
                clienteDao.findAll(PageRequest.of(0, 5));

        assertThat(pagina.getContent()).hasSize(1);

        assertThat(pagina.getContent().get(0).getNombre())
                .isEqualTo("Grace");
    }

    // =====================================================
    // QUERY PERSONALIZADA
    // =====================================================

    @Test
    @DisplayName("findAllRegiones — retorna regiones persistidas")
    void findAllRegiones_debeRetornarRegiones() {

        List<Region> regiones =
                clienteDao.findAllRegiones();

        assertThat(regiones).hasSize(1);

        assertThat(regiones.get(0).getNombre())
                .isEqualTo("Asia");
    }
}