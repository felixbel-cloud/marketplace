package com.bel.marketplace.service.impl;

import com.bel.marketplace.entity.Negocio;
import com.bel.marketplace.entity.Producto;
import com.bel.marketplace.entity.Reserva;
import com.bel.marketplace.entity.Usuario;
import com.bel.marketplace.repository.NegocioRepository;
import com.bel.marketplace.repository.ProductoRepository;
import com.bel.marketplace.repository.ReservaRepository;
import com.bel.marketplace.repository.UsuarioRepository;
import com.bel.marketplace.service.ReservaServiceInterface;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ReservaService implements ReservaServiceInterface {

    private final ReservaRepository reservaRepository;
    private final ProductoRepository productoRepository;
    private final NegocioRepository negocioRepository;
    private final UsuarioRepository usuarioRepository;

    public ReservaService(ReservaRepository reservaRepository,
                          ProductoRepository productoRepository,
                          NegocioRepository negocioRepository,
                          UsuarioRepository usuarioRepository) {
        this.reservaRepository = reservaRepository;
        this.productoRepository = productoRepository;
        this.negocioRepository = negocioRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public List<Reserva> listar() {
        return reservaRepository.findAll();
    }

    @Override
    public Reserva obtenerPorId(Long id) {
        return reservaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reserva no encontrada"));
    }

    @Override
    public Reserva crear(Reserva reserva) {
        validarYCompletarReserva(reserva, null);
        ajustarStock(reserva, null);
        return reservaRepository.save(reserva);
    }

    @Override
    public Reserva actualizar(Long id, Reserva datos) {
        Reserva existente = obtenerPorId(id);
        validarYCompletarReserva(datos, existente);
        ajustarStock(datos, existente);

        existente.setCodigoRetiro(datos.getCodigoRetiro());
        existente.setCantidad(datos.getCantidad());
        existente.setTotal(datos.getTotal());
        existente.setEstado(datos.getEstado());
        existente.setFechaReserva(datos.getFechaReserva());
        existente.setFechaHoraRetiro(datos.getFechaHoraRetiro());
        existente.setProducto(datos.getProducto());
        existente.setNegocio(datos.getNegocio());
        existente.setCliente(datos.getCliente());

        return reservaRepository.save(existente);
    }

    @Override
    public void eliminar(Long id) {
        Reserva reserva = obtenerPorId(id);
        restaurarStock(reserva);
        reservaRepository.delete(reserva);
    }

    @Override
    public List<Reserva> listarPorCliente(Long clienteId) {
        return reservaRepository.findByClienteId(clienteId);
    }

    @Override
    public List<Reserva> listarPorProducto(Long productoId) {
        return reservaRepository.findByProductoId(productoId);
    }

    private void validarYCompletarReserva(Reserva reserva, Reserva reservaOriginal) {
        if (reserva.getCantidad() == null || reserva.getCantidad() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La cantidad debe ser mayor a cero");
        }

        Producto producto = obtenerProducto(reserva.getProducto());
        reserva.setProducto(producto);

        Negocio negocio = obtenerNegocio(reserva.getNegocio(), producto);
        reserva.setNegocio(negocio);

        Usuario cliente = obtenerCliente(reserva.getCliente());
        reserva.setCliente(cliente);

        if (reserva.getFechaReserva() == null) {
            reserva.setFechaReserva(LocalDateTime.now());
        }

        double precioBase = producto.getPrecioDescuento() != null ? producto.getPrecioDescuento() : producto.getPrecioOriginal();
        reserva.setTotal(precioBase * reserva.getCantidad());

        if (reservaOriginal != null) {
            reserva.setId(reservaOriginal.getId());
            if (reserva.getCodigoRetiro() == null) {
                reserva.setCodigoRetiro(reservaOriginal.getCodigoRetiro());
            }
            if (reserva.getEstado() == null) {
                reserva.setEstado(reservaOriginal.getEstado());
            }
            if (reserva.getFechaHoraRetiro() == null) {
                reserva.setFechaHoraRetiro(reservaOriginal.getFechaHoraRetiro());
            }
        }
    }

    private void ajustarStock(Reserva nuevaReserva, Reserva reservaAnterior) {
        Producto productoActual = nuevaReserva.getProducto();
        Producto productoAnterior = reservaAnterior != null ? reservaAnterior.getProducto() : null;
        int cantidadAnterior = reservaAnterior != null ? reservaAnterior.getCantidad() : 0;

        if (productoAnterior != null && productoAnterior.getId().equals(productoActual.getId())) {
            int stockDisponible = productoRepository.findById(productoActual.getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"))
                    .getStock() + cantidadAnterior;
            if (nuevaReserva.getCantidad() > stockDisponible) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No hay stock suficiente para la cantidad solicitada");
            }
            productoActual.setStock(stockDisponible - nuevaReserva.getCantidad());
            productoRepository.save(productoActual);
        } else {
            if (productoAnterior != null) {
                productoAnterior.setStock(productoAnterior.getStock() + cantidadAnterior);
                productoRepository.save(productoAnterior);
            }
            if (nuevaReserva.getCantidad() > productoActual.getStock()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No hay stock suficiente para la cantidad solicitada");
            }
            productoActual.setStock(productoActual.getStock() - nuevaReserva.getCantidad());
            productoRepository.save(productoActual);
        }
    }

    private void restaurarStock(Reserva reserva) {
        Producto producto = reserva.getProducto();
        producto.setStock(producto.getStock() + reserva.getCantidad());
        productoRepository.save(producto);
    }

    private Producto obtenerProducto(Producto producto) {
        if (producto == null || producto.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe indicar el producto");
        }
        return productoRepository.findById(producto.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
    }

    private Negocio obtenerNegocio(Negocio negocio, Producto producto) {
        Negocio negocioEncontrado;
        if (negocio != null && negocio.getId() != null) {
            negocioEncontrado = negocioRepository.findById(negocio.getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Negocio no encontrado"));
        } else {
            negocioEncontrado = producto.getNegocio();
        }
        if (!producto.getNegocio().getId().equals(negocioEncontrado.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El producto no pertenece al negocio indicado");
        }
        return negocioEncontrado;
    }

    private Usuario obtenerCliente(Usuario cliente) {
        if (cliente == null || cliente.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe indicar el cliente");
        }
        return usuarioRepository.findById(cliente.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));
    }
}
