package com.Mansion.HabitacionesMC.DTO;

import lombok.Data;

@Data
public class ObjetosDTO {

    private Long idInstancia;
    private Long idObjeto;
    private String tipoObjeto;
    private String nombreObjeto;
    private String nombreHabitacion;
    private String estado;
    private Integer cantidad;

}
