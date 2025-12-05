package com.bel.marketplace.service;

import com.bel.marketplace.entity.Negocio;

import java.util.List;

public interface NegocioServiceInterface {

    List<Negocio> listar();

    Negocio obtenerPorId(Long id);

    Negocio crear(Negocio negocio);

    Negocio actualizar(Long id, Negocio negocio);

    void eliminar(Long id);
}
