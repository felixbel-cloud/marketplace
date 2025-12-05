package com.bel.marketplace.service;

import com.bel.marketplace.entity.Usuario;

import java.util.List;

public interface UsuarioServiceInterface {

    List<Usuario> listar();

    Usuario obtenerPorId(Long id);

    Usuario crear(Usuario usuario);

    Usuario actualizar(Long id, Usuario usuario);

    void eliminar(Long id);
}
