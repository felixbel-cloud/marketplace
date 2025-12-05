package com.bel.marketplace.controller;

import com.bel.marketplace.entity.Reserva;
import com.bel.marketplace.repository.ReservaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    private final ReservaRepository reservaRepository;

    public ReservaController(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    @GetMapping
    public ResponseEntity<List<Reserva>> listarTodos() {
        return ResponseEntity.ok(reservaRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reserva> obtenerPorId(@PathVariable Long id) {
        Optional<Reserva> reserva = reservaRepository.findById(id);
        return reserva.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Reserva> crear(@RequestBody Reserva reserva) {
        Reserva creada = reservaRepository.save(reserva);
        return ResponseEntity.created(URI.create("/api/reservas/" + creada.getId())).body(creada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reserva> actualizar(@PathVariable Long id, @RequestBody Reserva datos) {
        Optional<Reserva> existente = reservaRepository.findById(id);

        if (existente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Reserva reserva = existente.get();
        reserva.setCodigoRetiro(datos.getCodigoRetiro());
        reserva.setCantidad(datos.getCantidad());
        reserva.setTotal(datos.getTotal());
        reserva.setEstado(datos.getEstado());
        reserva.setFechaReserva(datos.getFechaReserva());
        reserva.setFechaHoraRetiro(datos.getFechaHoraRetiro());
        reserva.setProducto(datos.getProducto());
        reserva.setNegocio(datos.getNegocio());
        reserva.setCliente(datos.getCliente());

        Reserva actualizada = reservaRepository.save(reserva);
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        Optional<Reserva> existente = reservaRepository.findById(id);
        if (existente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        reservaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
