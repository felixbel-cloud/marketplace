package com.bel.marketplace.service.impl;

import com.bel.marketplace.entity.Negocio;
import com.bel.marketplace.entity.Producto;
import com.bel.marketplace.repository.NegocioRepository;
import com.bel.marketplace.repository.ProductoRepository;
import com.bel.marketplace.service.ProductoServiceInterface;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class ProductoService implements ProductoServiceInterface {

    private final ProductoRepository productoRepository;
    private final NegocioRepository negocioRepository;

    public ProductoService(ProductoRepository productoRepository, NegocioRepository negocioRepository) {
        this.productoRepository = productoRepository;
        this.negocioRepository = negocioRepository;
    }

    @Override
    public List<Producto> listar() {
        return productoRepository.findAll();
    }

    @Override
    public Producto obtenerPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
    }

    @Override
    public Producto crear(Producto producto) {
        asignarNegocio(producto);
        validarProducto(producto);
        return productoRepository.save(producto);
    }

    @Override
    public Producto actualizar(Long id, Producto datos) {
        Producto existente = obtenerPorId(id);

        existente.setNombre(datos.getNombre());
        existente.setCategoria(datos.getCategoria());
        existente.setDescripcion(datos.getDescripcion());
        existente.setPrecioOriginal(datos.getPrecioOriginal());
        existente.setPrecioDescuento(datos.getPrecioDescuento());
        existente.setStock(datos.getStock());
        existente.setFechaVencimiento(datos.getFechaVencimiento());
        if (datos.getNegocio() != null) {
            existente.setNegocio(datos.getNegocio());
            asignarNegocio(existente);
        }

        validarProducto(existente);
        return productoRepository.save(existente);
    }

    @Override
    public void eliminar(Long id) {
        Producto producto = obtenerPorId(id);
        productoRepository.delete(producto);
    }

    @Override
    public List<Producto> listarPorCategoria(String categoria) {
        return productoRepository.findByCategoria(categoria);
    }

    @Override
    public List<Producto> proximosAVencer(int dias) {
        LocalDate hoy = LocalDate.now();
        LocalDate limite = hoy.plusDays(dias);
        return productoRepository.findByFechaVencimientoBetween(hoy, limite);
    }

    @Override
    public List<Producto> listarPorNegocio(Long negocioId) {
        return productoRepository.findByNegocioId(negocioId);
    }

    private void asignarNegocio(Producto producto) {
        if (producto.getNegocio() == null || producto.getNegocio().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El producto debe pertenecer a un negocio");
        }
        Negocio negocio = negocioRepository.findById(producto.getNegocio().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Negocio no encontrado"));
        producto.setNegocio(negocio);
    }

    private void validarProducto(Producto producto) {
        if (producto.getNombre() == null || producto.getNombre().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre es obligatorio");
        }
        if (producto.getPrecioOriginal() == null || producto.getPrecioOriginal() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El precio original debe ser mayor a cero");
        }
        if (producto.getStock() == null || producto.getStock() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El stock no puede ser negativo");
        }
        if (producto.getPrecioDescuento() != null && producto.getPrecioDescuento() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El precio con descuento debe ser mayor a cero");
        }
    }
}
