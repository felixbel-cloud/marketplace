package com.bel.marketplace.controller;

import com.bel.marketplace.entity.Negocio;
import com.bel.marketplace.entity.Producto;
import com.bel.marketplace.service.NegocioServiceInterface;
import com.bel.marketplace.service.ProductoServiceInterface;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/negocios")
@CrossOrigin("*")
public class NegocioController {

    private final NegocioServiceInterface negocioService;
    private final ProductoServiceInterface productoService;

    public NegocioController(NegocioServiceInterface negocioService, ProductoServiceInterface productoService) {
        this.negocioService = negocioService;
        this.productoService = productoService;
    }

    @GetMapping
    public ResponseEntity<List<Negocio>> listar() {
        return ResponseEntity.ok(negocioService.listar());
    }

    @PostMapping
    public ResponseEntity<Negocio> registrar(@RequestBody Negocio negocio) {
        Negocio creado = negocioService.crear(negocio);
        return ResponseEntity.created(URI.create("/api/negocios/" + creado.getId())).body(creado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Negocio> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(negocioService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Negocio> actualizar(@PathVariable Long id, @RequestBody Negocio datos) {
        Negocio actualizado = negocioService.actualizar(id, datos);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        negocioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/productos")
    public ResponseEntity<List<Producto>> listarProductosPorNegocio(@PathVariable Long id) {
        negocioService.obtenerPorId(id);
        return ResponseEntity.ok(productoService.listarPorNegocio(id));
    }
}
