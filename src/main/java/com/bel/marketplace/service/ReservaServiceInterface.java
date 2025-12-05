package com.bel.marketplace.service;

import com.bel.marketplace.entity.Reserva;

import java.util.List;

public interface ReservaServiceInterface {

    List<Reserva> listar();

    Reserva obtenerPorId(Long id);

    Reserva crear(Reserva reserva);

    Reserva actualizar(Long id, Reserva reserva);

    void eliminar(Long id);

    List<Reserva> listarPorCliente(Long clienteId);

    List<Reserva> listarPorProducto(Long productoId);
}
