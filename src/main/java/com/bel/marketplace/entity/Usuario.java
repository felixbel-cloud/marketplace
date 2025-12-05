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
@EqualsAndHashCode(exclude = {"negocios", "reservas"})
@ToString(exclude = {"negocios", "reservas"})
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombreCompleto;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password; // luego puedes encriptarla

    @Enumerated(EnumType.STRING)
    private RolUsuario rol;

    private String telefono;

    @OneToMany(mappedBy = "usuario")
    @JsonIgnoreProperties("usuario")
    private List<Negocio> negocios;

    @OneToMany(mappedBy = "cliente")
    @JsonIgnoreProperties({"cliente", "negocio", "producto"})
    private List<Reserva> reservas;
}
