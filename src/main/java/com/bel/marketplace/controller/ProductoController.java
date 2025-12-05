package com.bel.marketplace.controller;

import com.bel.marketplace.dto.ProductoRequest;
import com.bel.marketplace.entity.Negocio;
import com.bel.marketplace.entity.Producto;
import com.bel.marketplace.service.ProductoServiceInterface;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoServiceInterface productoService;

    public ProductoController(ProductoServiceInterface productoService) {
        this.productoService = productoService;
    }

    // Listar todos los productos
    @GetMapping
    public ResponseEntity<List<Producto>> listarTodos() {
        return ResponseEntity.ok(productoService.listar());
    }

    // Buscar producto por id
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.obtenerPorId(id));
    }

    // Crear producto
    @PostMapping
    public ResponseEntity<Producto> crear(@RequestBody ProductoRequest request) {
        Producto creado = productoService.crear(mapearProducto(request));
        return ResponseEntity.created(URI.create("/api/productos/" + creado.getId())).body(creado);
    }

    // Actualizar producto
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable Long id, @RequestBody ProductoRequest request) {
        Producto actualizado = productoService.actualizar(id, mapearProducto(request));
        return ResponseEntity.ok(actualizado);
    }

    // Eliminar producto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // Listar por categoría
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Producto>> listarPorCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(productoService.listarPorCategoria(categoria));
    }

    // Productos próximos a vencer (ej: próximos X días)
    @GetMapping("/proximos-vencer")
    public ResponseEntity<List<Producto>> proximosAVencer(@RequestParam(name = "dias", defaultValue = "3") int dias) {
        return ResponseEntity.ok(productoService.proximosAVencer(dias));
    }

    private Producto mapearProducto(ProductoRequest request) {
        Producto producto = new Producto();
        producto.setNombre(request.getNombre());
        producto.setCategoria(request.getCategoria());
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecioOriginal(request.getPrecioOriginal());
        producto.setPrecioDescuento(request.getPrecioDescuento());
        producto.setStock(request.getStock());
        producto.setFechaVencimiento(request.getFechaVencimiento());
        if (request.getNegocioId() != null) {
            producto.setNegocio(crearNegocioConId(request.getNegocioId()));
        }
        return producto;
    }

    private Negocio crearNegocioConId(Long negocioId) {
        Negocio negocio = new Negocio();
        negocio.setId(negocioId);
        return negocio;
    }
}
