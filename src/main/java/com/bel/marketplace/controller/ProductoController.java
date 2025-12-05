package com.bel.marketplace.controller;

import com.bel.marketplace.entity.Producto;
import com.bel.marketplace.repository.ProductoRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoRepository productoRepository;

    public ProductoController(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    // Listar todos los productos
    @GetMapping
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    // Buscar producto por id
    @GetMapping("/{id}")
    public Producto obtenerPorId(@PathVariable Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    // Crear producto
    @PostMapping
    public Producto crear(@RequestBody Producto producto) {
        return productoRepository.save(producto);
    }

    // Actualizar producto
    @PutMapping("/{id}")
    public Producto actualizar(@PathVariable Long id, @RequestBody Producto datos) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        producto.setNombre(datos.getNombre());
        producto.setCategoria(datos.getCategoria());
        producto.setDescripcion(datos.getDescripcion());
        producto.setPrecioOriginal(datos.getPrecioOriginal());
        producto.setPrecioDescuento(datos.getPrecioDescuento());
        producto.setStock(datos.getStock());
        producto.setFechaVencimiento(datos.getFechaVencimiento());
        producto.setNegocio(datos.getNegocio());

        return productoRepository.save(producto);
    }

    // Eliminar producto
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        productoRepository.deleteById(id);
    }

    // Listar por categoría
    @GetMapping("/categoria/{categoria}")
    public List<Producto> listarPorCategoria(@PathVariable String categoria) {
        return productoRepository.findByCategoria(categoria);
    }

    // Productos próximos a vencer (ej: próximos X días)
    @GetMapping("/proximos-vencer")
    public List<Producto> proximosAVencer(@RequestParam(defaultValue = "3") int dias) {
        LocalDate hoy = LocalDate.now();
        LocalDate limite = hoy.plusDays(dias);
        return productoRepository.findByFechaVencimientoBetween(hoy, limite);
    }
}