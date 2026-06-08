package com.Mansion.HabitacionesMC.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "EVENTO")
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEvento;

    @NotNull(message = "El evento debe tener tipo o categoría asignada")
    @ManyToOne
    @JoinColumn(name = "id_eventos")
    private Eventos tipoEvento;

    @NotNull(message = "Debe indicar en qué habitación ocurre")
    @ManyToOne
    @JoinColumn(name = "id_habitacion")
    private Habitacion habitacion;

    @NotBlank(message = "Descripción necesaria para contexto")
    private String descripcionEspecifica;

    private boolean completado;

}
