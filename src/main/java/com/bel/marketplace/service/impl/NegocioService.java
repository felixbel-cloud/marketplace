package com.bel.marketplace.service.impl;

import com.bel.marketplace.entity.Negocio;
import com.bel.marketplace.entity.Usuario;
import com.bel.marketplace.entity.Producto;
import com.bel.marketplace.repository.NegocioRepository;
import com.bel.marketplace.repository.ProductoRepository;
import com.bel.marketplace.repository.UsuarioRepository;
import com.bel.marketplace.service.NegocioServiceInterface;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class NegocioService implements NegocioServiceInterface {

    private final NegocioRepository negocioRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;

    public NegocioService(NegocioRepository negocioRepository, UsuarioRepository usuarioRepository, ProductoRepository productoRepository) {
        this.negocioRepository = negocioRepository;
        this.usuarioRepository = usuarioRepository;
        this.productoRepository = productoRepository;
    }

    @Override
    public List<Negocio> listar() {
        return negocioRepository.findAll();
    }

    @Override
    public Negocio obtenerPorId(Long id) {
        return negocioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Negocio no encontrado"));
    }

    @Override
    public Negocio crear(Negocio negocio) {
        asignarUsuario(negocio);
        validarNegocio(negocio);
        return negocioRepository.save(negocio);
    }

    @Override
    public Negocio actualizar(Long id, Negocio datos) {
        Negocio existente = obtenerPorId(id);

        existente.setNombre(datos.getNombre());
        existente.setDireccion(datos.getDireccion());
        existente.setDistrito(datos.getDistrito());
        existente.setTelefono(datos.getTelefono());
        if (datos.getUsuario() != null) {
            existente.setUsuario(datos.getUsuario());
            asignarUsuario(existente);
        }

        validarNegocio(existente);
        return negocioRepository.save(existente);
    }

    @Override
    public void eliminar(Long id) {
        Negocio negocio = obtenerPorId(id);
        negocioRepository.delete(negocio);
    }

    @Override
    public List<Producto> obtenerProductos(Long negocioId) {
        Negocio negocio = obtenerPorId(negocioId);
        return productoRepository.findByNegocioId(negocio.getId());
    }

    private void asignarUsuario(Negocio negocio) {
        if (negocio.getUsuario() == null || negocio.getUsuario().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El negocio debe tener un dueño");
        }
        Usuario usuario = usuarioRepository.findById(negocio.getUsuario().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        negocio.setUsuario(usuario);
    }

    private void validarNegocio(Negocio negocio) {
        if (negocio.getNombre() == null || negocio.getNombre().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre es obligatorio");
        }
        if (negocio.getDireccion() == null || negocio.getDireccion().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La dirección es obligatoria");
        }
        if (negocio.getDistrito() == null || negocio.getDistrito().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El distrito es obligatorio");
        }
        if (negocio.getTelefono() == null || negocio.getTelefono().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El teléfono es obligatorio");
        }
    }
}
