package com.bel.marketplace.service;

import com.bel.marketplace.entity.Producto;

import java.util.List;

public interface ProductoServiceInterface {

    List<Producto> listar();

    Producto obtenerPorId(Long id);

    Producto crear(Producto producto);

    Producto actualizar(Long id, Producto producto);

    void eliminar(Long id);

    List<Producto> listarPorCategoria(String categoria);

    List<Producto> proximosAVencer(int dias);

    List<Producto> listarPorNegocio(Long negocioId);
}
