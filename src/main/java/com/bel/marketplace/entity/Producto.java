package com.bel.marketplace.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import com.bel.marketplace.entity.Negocio;

@Data
@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String categoria; // Lácteos, Bebidas, Snacks, etc.

    private String descripcion;

    private Double precioOriginal;

    private Double precioDescuento;

    private Integer stock;

    private LocalDate fechaVencimiento;

    @ManyToOne
    @JoinColumn(name = "negocio_id")
    private Negocio negocio;
    
}
