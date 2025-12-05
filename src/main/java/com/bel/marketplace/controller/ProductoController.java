package com.bel.marketplace.controller;

import com.bel.marketplace.entity.Producto;
import com.bel.marketplace.repository.ProductoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoRepository productoRepository;

    public ProductoController(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    // Listar todos los productos
    @GetMapping
    public ResponseEntity<List<Producto>> listarTodos() {
        return ResponseEntity.ok(productoRepository.findAll());
    }

    // Buscar producto por id
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Long id) {
        Optional<Producto> producto = productoRepository.findById(id);
        return producto.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crear producto
    @PostMapping
    public ResponseEntity<Producto> crear(@RequestBody Producto producto) {
        Producto creado = productoRepository.save(producto);
        return ResponseEntity.created(URI.create("/api/productos/" + creado.getId())).body(creado);
    }

    // Actualizar producto
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable Long id, @RequestBody Producto datos) {
        Optional<Producto> existente = productoRepository.findById(id);

        if (existente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Producto producto = existente.get();
        producto.setNombre(datos.getNombre());
        producto.setCategoria(datos.getCategoria());
        producto.setDescripcion(datos.getDescripcion());
        producto.setPrecioOriginal(datos.getPrecioOriginal());
        producto.setPrecioDescuento(datos.getPrecioDescuento());
        producto.setStock(datos.getStock());
        producto.setFechaVencimiento(datos.getFechaVencimiento());
        producto.setNegocio(datos.getNegocio());

        Producto actualizado = productoRepository.save(producto);
        return ResponseEntity.ok(actualizado);
    }

    // Eliminar producto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        Optional<Producto> existente = productoRepository.findById(id);
        if (existente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        productoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Listar por categoría
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Producto>> listarPorCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(productoRepository.findByCategoria(categoria));
    }

    // Productos próximos a vencer (ej: próximos X días)
    @GetMapping("/proximos-vencer")
    public ResponseEntity<List<Producto>> proximosAVencer(@RequestParam(name = "dias", defaultValue = "3") int dias) {
        LocalDate hoy = LocalDate.now();
        LocalDate limite = hoy.plusDays(dias);
        return ResponseEntity.ok(productoRepository.findByFechaVencimientoBetween(hoy, limite));
    }
}
