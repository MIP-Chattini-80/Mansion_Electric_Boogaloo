package com.Mansion.HabitacionesMC.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Mansion.HabitacionesMC.DTO.PuertaDTO;
import com.Mansion.HabitacionesMC.Model.Puerta;
import com.Mansion.HabitacionesMC.Repository.PuertaRepository;

@Transactional
@Service
public class PuertaService {

    @Autowired
    private PuertaRepository puertaRepository;

    private PuertaDTO mapToDTO(Puerta puerta) {
        if (puerta == null) return null;
        return puerta.convertirADTO();
    }

    public List<PuertaDTO> listarTodas() {
        return puertaRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public PuertaDTO obtenerPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID de la puerta no puede ser nulo.");
        }
        return puertaRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Puerta no encontrada con el ID: " + id));
    }

    public PuertaDTO guardarPuerta(Puerta puerta) {
        if (puerta == null) {
            throw new IllegalArgumentException("El objeto Puerta no puede ser nulo.");
        }
        if (puerta.getOrigen() == null || puerta.getOrigen().getIdHabitacion() == null) {
            throw new IllegalArgumentException("La puerta debe estar vinculada a una habitación de origen válida.");
        }
        if (puerta.getDestino() == null || puerta.getDestino().getIdHabitacion() == null) {
            throw new IllegalArgumentException("La puerta debe estar vinculada a una habitación de destino válida.");
        }
        if (puerta.getOrigen().getIdHabitacion().equals(puerta.getDestino().getIdHabitacion())) {
            throw new IllegalArgumentException("La habitación de origen y destino no pueden ser la misma.");
        }

        Puerta guardada = puertaRepository.save(puerta);
        return mapToDTO(guardada);
    }

    public PuertaDTO actualizarPuerta(Long id, Puerta datosNuevos) {
        if (id == null || datosNuevos == null) {
            throw new IllegalArgumentException("El ID o los datos de actualización no pueden ser nulos.");
        }
        Puerta puertaExistente = puertaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se puede actualizar. La puerta no existe con el ID: " + id));
        if (datosNuevos.getOrigen() == null || datosNuevos.getDestino() == null) {
            throw new IllegalArgumentException("Las habitaciones de origen y destino son estrictamente obligatorias en PUT.");
        }
        if (datosNuevos.getOrigen().getIdHabitacion().equals(datosNuevos.getDestino().getIdHabitacion())) {
            throw new IllegalArgumentException("La habitación de origen y destino no pueden ser idénticas.");
        }
        puertaExistente.setOrigen(datosNuevos.getOrigen());
        puertaExistente.setDestino(datosNuevos.getDestino());
        puertaExistente.setEstaBloqueada(datosNuevos.isEstaBloqueada());
        Puerta actualizada = puertaRepository.save(puertaExistente);
        return mapToDTO(actualizada);
    }

    public PuertaDTO editarPuerta(Long id, PuertaDTO datosNuevos) {
        if (id == null || datosNuevos == null) {
            throw new IllegalArgumentException("El ID o los datos para editar no pueden ser nulos.");
        }
        Puerta puerta = puertaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se puede editar. La puerta no existe con el ID: " + id));
        puerta.setEstaBloqueada(datosNuevos.isEstaBloqueada());

        Puerta editada = puertaRepository.save(puerta);
        return mapToDTO(editada);
    }

    public void eliminar(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID de la puerta no puede ser nulo.");
        }
        if (!puertaRepository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar. Puerta no encontrada con el ID: " + id);
        }
        puertaRepository.deleteById(id);
    }

}
