package com.bel.marketplace.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "negocios")
public class Negocio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String direccion;

    private String distrito; // Magdalena del Mar, San Miguel

    private String telefono;

    @OneToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario; // dueño del negocio
}
