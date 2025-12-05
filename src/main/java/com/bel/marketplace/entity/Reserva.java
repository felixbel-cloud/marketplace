package com.bel.marketplace.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"producto", "cliente", "negocio"})
@ToString(exclude = {"producto", "cliente", "negocio"})
@Entity
@Table(name = "reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codigoRetiro; // alfanumérico (ej. generado luego)

    private Integer cantidad;

    private Double total;

    @Enumerated(EnumType.STRING)
    private EstadoReserva estado;

    private LocalDateTime fechaReserva;

    private LocalDateTime fechaHoraRetiro; // ventana/hora estimada de retiro

    @ManyToOne
    @JoinColumn(name = "producto_id")
    @JsonIgnoreProperties({"negocio"})
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "negocio_id")
    @JsonIgnoreProperties({"productos", "reservas", "usuario"})
    private Negocio negocio;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    @JsonIgnoreProperties({"negocios", "reservas"})
    private Usuario cliente;
}
