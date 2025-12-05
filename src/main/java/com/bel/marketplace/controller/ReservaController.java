package com.bel.marketplace.controller;

import com.bel.marketplace.entity.Reserva;
import com.bel.marketplace.service.ReservaServiceInterface;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    private final ReservaServiceInterface reservaService;

    public ReservaController(ReservaServiceInterface reservaService) {
        this.reservaService = reservaService;
    }

    @GetMapping
    public ResponseEntity<List<Reserva>> listarTodos() {
        return ResponseEntity.ok(reservaService.listar());
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Reserva>> listarPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(reservaService.listarPorCliente(clienteId));
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<Reserva>> listarPorProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(reservaService.listarPorProducto(productoId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reserva> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(reservaService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<Reserva> crear(@RequestBody Reserva reserva) {
        Reserva creada = reservaService.crear(reserva);
        return ResponseEntity.created(URI.create("/api/reservas/" + creada.getId())).body(creada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reserva> actualizar(@PathVariable Long id, @RequestBody Reserva datos) {
        Reserva actualizada = reservaService.actualizar(id, datos);
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        reservaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
