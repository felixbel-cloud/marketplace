package com.bel.marketplace.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "negocio")
@ToString(exclude = "negocio")
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
    @JsonIgnoreProperties({"productos", "reservas", "usuario"})
    private Negocio negocio;

}
