package com.bel.marketplace.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"productos", "reservas"})
@ToString(exclude = {"productos", "reservas", "usuario"})
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

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    @JsonIgnoreProperties({"negocios", "reservas"})
    private Usuario usuario; // dueño del negocio

    @OneToMany(mappedBy = "negocio")
    @JsonIgnoreProperties("negocio")
    private List<Producto> productos;

    @OneToMany(mappedBy = "negocio")
    @JsonIgnoreProperties({"negocio", "cliente", "producto"})
    private List<Reserva> reservas;
}
