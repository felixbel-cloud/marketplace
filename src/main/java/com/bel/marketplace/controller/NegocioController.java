package com.bel.marketplace.controller;

import com.bel.marketplace.entity.Negocio;
import com.bel.marketplace.repository.NegocioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/negocios")
@CrossOrigin("*")
public class NegocioController {

    private final NegocioRepository negocioRepository;

    public NegocioController(NegocioRepository negocioRepository) {
        this.negocioRepository = negocioRepository;
    }

    @GetMapping
    public ResponseEntity<List<Negocio>> listar() {
        return ResponseEntity.ok(negocioRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<Negocio> registrar(@RequestBody Negocio negocio) {
        Negocio creado = negocioRepository.save(negocio);
        return ResponseEntity.created(URI.create("/api/negocios/" + creado.getId())).body(creado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Negocio> obtener(@PathVariable Long id) {
        Optional<Negocio> negocio = negocioRepository.findById(id);
        return negocio.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Negocio> actualizar(@PathVariable Long id, @RequestBody Negocio datos) {
        Optional<Negocio> existente = negocioRepository.findById(id);

        if (existente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Negocio negocio = existente.get();
        negocio.setNombre(datos.getNombre());
        negocio.setDireccion(datos.getDireccion());
        negocio.setDistrito(datos.getDistrito());
        negocio.setTelefono(datos.getTelefono());
        negocio.setUsuario(datos.getUsuario());

        Negocio actualizado = negocioRepository.save(negocio);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        Optional<Negocio> existente = negocioRepository.findById(id);
        if (existente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        negocioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
