package com.bel.marketplace.controller;

import com.bel.marketplace.entity.RolUsuario;
import com.bel.marketplace.entity.Usuario;
import com.bel.marketplace.service.UsuarioServiceInterface;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<Map<String, Object>> crear(@RequestBody Map<String, Object> body) {
        Usuario usuario = mapearUsuario(body);
        Usuario creado = usuarioService.crear(usuario);

        Map<String, Object> response = new HashMap<>();
        response.put("id", creado.getId());
        response.put("nombre", creado.getNombreCompleto());
        response.put("email", creado.getEmail());
        response.put("rol", creado.getRol());

        return ResponseEntity.created(URI.create("/api/usuarios/" + creado.getId())).body(response);
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

    private Usuario mapearUsuario(Map<String, Object> body) {
        Usuario usuario = new Usuario();
        usuario.setNombreCompleto((String) body.get("nombre"));
        usuario.setEmail((String) body.get("email"));
        usuario.setPassword((String) body.get("contrasena"));

        String rolStr = (String) body.get("rol");
        RolUsuario rol = RolUsuario.CLIENTE;

        if (rolStr != null && !rolStr.trim().isEmpty()) {
            if ("NEGOCIO".equalsIgnoreCase(rolStr.trim())) {
                rol = RolUsuario.NEGOCIO;
            }
        }

        usuario.setRol(rol);
        return usuario;
    }
}
