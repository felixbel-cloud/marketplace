package com.bel.marketplace.repository;

import com.bel.marketplace.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByCategoria(String categoria);

    List<Producto> findByFechaVencimientoBetween(LocalDate desde, LocalDate hasta);

    List<Producto> findByNegocioId(Long negocioId);
}