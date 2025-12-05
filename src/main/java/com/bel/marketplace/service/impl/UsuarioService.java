package com.bel.marketplace.service.impl;

import com.bel.marketplace.entity.Usuario;
import com.bel.marketplace.repository.UsuarioRepository;
import com.bel.marketplace.service.UsuarioServiceInterface;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UsuarioService implements UsuarioServiceInterface {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario obtenerPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    }

    @Override
    public Usuario crear(Usuario usuario) {
        validarUsuario(usuario, null);
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario actualizar(Long id, Usuario datos) {
        Usuario existente = obtenerPorId(id);

        existente.setNombreCompleto(datos.getNombreCompleto());
        existente.setEmail(datos.getEmail());
        existente.setPassword(datos.getPassword());
        existente.setRol(datos.getRol());
        existente.setTelefono(datos.getTelefono());

        validarUsuario(existente, id);
        return usuarioRepository.save(existente);
    }

    @Override
    public void eliminar(Long id) {
        Usuario usuario = obtenerPorId(id);
        usuarioRepository.delete(usuario);
    }

    private void validarUsuario(Usuario usuario, Long idActual) {
        if (usuario.getEmail() == null || usuario.getEmail().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El email es obligatorio");
        }
        if (usuario.getPassword() == null || usuario.getPassword().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La contraseña es obligatoria");
        }
        Usuario existente = usuarioRepository.findByEmail(usuario.getEmail());
        if (existente != null && (idActual == null || !existente.getId().equals(idActual))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El email ya está registrado");
        }
    }
}
