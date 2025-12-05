package com.bel.marketplace.controller;

import com.bel.marketplace.dto.UsuarioRequest;
import com.bel.marketplace.entity.RolUsuario;
import com.bel.marketplace.entity.Usuario;
import com.bel.marketplace.service.UsuarioServiceInterface;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioServiceInterface usuarioService;

    public UsuarioController(UsuarioServiceInterface usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> listarTodos() {
        return ResponseEntity.ok(usuarioService.listar());
    }

    @GetMapping("/buscar")
    public ResponseEntity<Usuario> obtenerPorEmail(@RequestParam String email) {
        return ResponseEntity.ok(usuarioService.obtenerPorEmail(email));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<Usuario> crear(@RequestBody UsuarioRequest request) {
        try {
            Usuario usuario = mapearUsuario(request);
            Usuario creado = usuarioService.crear(usuario);
            return ResponseEntity.created(URI.create("/api/usuarios/" + creado.getId())).body(creado);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rol inválido. Usa CLIENTE o NEGOCIO");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizar(@PathVariable Long id, @RequestBody Usuario datos) {
        Usuario actualizado = usuarioService.actualizar(id, datos);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private Usuario mapearUsuario(UsuarioRequest request) {
        Usuario usuario = new Usuario();
        usuario.setNombreCompleto(request.getNombre());
        usuario.setEmail(request.getEmail());
        usuario.setPassword(request.getContrasena());

        String rolString = request.getRol();
        RolUsuario rol;
        if (rolString == null || rolString.trim().isEmpty()) {
            rol = RolUsuario.CLIENTE;
        } else {
            switch (rolString.trim().toUpperCase(Locale.ROOT)) {
                case "CLIENTE":
                    rol = RolUsuario.CLIENTE;
                    break;
                case "NEGOCIO":
                    rol = RolUsuario.NEGOCIO;
                    break;
                default:
                    throw new IllegalArgumentException("Rol inválido");
            }
        }
        usuario.setRol(rol);
        return usuario;
    }
}
