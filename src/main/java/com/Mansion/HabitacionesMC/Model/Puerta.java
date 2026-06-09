package com.Mansion.HabitacionesMC.Model;

import com.Mansion.HabitacionesMC.DTO.PuertaDTO;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PUERTA")
public class Puerta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPuerta;

    @NotNull(message = "Debe tener habitación de origen")
    @ManyToOne
    @JoinColumn(name = "id_habitacion_origen")
    private Habitacion origen;

    @NotNull(message = "La habitación debe tener destino")
    @ManyToOne
    @JoinColumn(name = "id_habitacion_destino")
    private Habitacion destino;

    private boolean estaBloqueada;

    public PuertaDTO convertirADTO() {
        PuertaDTO dto = new PuertaDTO();
        dto.setIdPuerta(this.idPuerta);
        dto.setEstaBloqueada(this.estaBloqueada);
        if (this.origen != null) {
            dto.setIdHabitacionOrigen(this.origen.getIdHabitacion());
        }
        if (this.destino != null) {
            dto.setIdHabitacionDestino(this.destino.getIdHabitacion());
        }
        return dto;
    }

}
