package com.bel.marketplace.controller;

import com.bel.marketplace.entity.Negocio;
import com.bel.marketplace.repository.NegocioRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/negocios")
@CrossOrigin("*")
public class NegocioController {

    private final NegocioRepository negocioRepository;

    public NegocioController(NegocioRepository negocioRepository) {
        this.negocioRepository = negocioRepository;
    }

    @GetMapping
    public List<Negocio> listar() {
        return negocioRepository.findAll();
    }

    @PostMapping
    public Negocio registrar(@RequestBody Negocio negocio) {
        return negocioRepository.save(negocio);
    }

    @GetMapping("/{id}")
    public Negocio obtener(@PathVariable Long id) {
        return negocioRepository.findById(id).orElse(null);
    }
}