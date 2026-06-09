package com.Mansion.HabitacionesMC.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Mansion.HabitacionesMC.DTO.HabitacionDTO;
import com.Mansion.HabitacionesMC.Model.Habitacion;
import com.Mansion.HabitacionesMC.Repository.HabitacionRepository;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class HabitacionService {

    @Autowired
    private HabitacionRepository habitacionRepository;

    private HabitacionDTO mapToDTO(Habitacion habitacion) {
        HabitacionDTO dto = new HabitacionDTO();
        dto.setIdHabitacion(habitacion.getIdHabitacion());
        dto.setNombre(habitacion.getNombre());
        dto.setDescripcion(habitacion.getDescripcion());
        dto.setEsZonaSegura(habitacion.isEsZonaSegura());
        return dto;
    }

    public List<HabitacionDTO> listarTodas() {
        return habitacionRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public HabitacionDTO obtenerPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo.");
        }
        return habitacionRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Habitación no encontrada"));
    }

    public HabitacionDTO guardarHabitacion(Habitacion habitacion) {
        if (habitacion == null) {
            throw new IllegalArgumentException("El objeto Habitacion no puede ser nulo.");
        }
        if (habitacion.getNombre() == null || habitacion.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la habitación es estrictamente obligatorio.");
        }
        String nombreSanitizado = habitacion.getNombre().trim().replaceAll("\\s+", " ");
        Habitacion existente = habitacionRepository.findByNombreIgnoreCase(nombreSanitizado);
        if (existente != null) {
            throw new RuntimeException("Ya existe una habitación registrada con el nombre: '" + nombreSanitizado + "'");
        }
        habitacion.setNombre(nombreSanitizado);
        if (habitacion.getDescripcion() != null) {
            habitacion.setDescripcion(habitacion.getDescripcion().trim().replaceAll("\\s+", " "));
        }
        Habitacion guardada = habitacionRepository.save(habitacion);
        return mapToDTO(guardada);
    }

    public HabitacionDTO actualizarHabitacion(Long id, Habitacion datosNuevos) {
        if (id == null || datosNuevos == null) {
            throw new IllegalArgumentException("El ID o los datos de actualización no pueden ser nulos.");
        }
        Habitacion habitacionExistente = habitacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se puede actualizar. La habitación no existe con el ID: " + id));
        if (datosNuevos.getNombre() == null || datosNuevos.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede quedar vacío al actualizar.");
        }
        String nuevoNombre = datosNuevos.getNombre().trim().replaceAll("\\s+", " ");
        if (!nuevoNombre.equalsIgnoreCase(habitacionExistente.getNombre())) {
            Habitacion duplicado = habitacionRepository.findByNombreIgnoreCase(nuevoNombre);
            if (duplicado != null) {
                throw new RuntimeException("No se puede actualizar. Ya existe otra habitación llamada: '" + nuevoNombre + "'");
            }
        }
        habitacionExistente.setNombre(nuevoNombre);
        if (datosNuevos.getDescripcion() != null) {
            habitacionExistente.setDescripcion(datosNuevos.getDescripcion().trim().replaceAll("\\s+", " "));
        } else {
            habitacionExistente.setDescripcion(null);
        }
        habitacionExistente.setEsZonaSegura(datosNuevos.isEsZonaSegura());
        Habitacion actualizada = habitacionRepository.save(habitacionExistente);
        return mapToDTO(actualizada);
    }

    public HabitacionDTO editarHabitacion(Long id, Habitacion datosNuevos) {
        if (id == null || datosNuevos == null) {
            throw new IllegalArgumentException("El ID o los datos para editar no pueden ser nulos.");
        }
        Habitacion habitacion = habitacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se puede editar. La habitación no existe con el ID: " + id));
        if (datosNuevos.getNombre() != null && !datosNuevos.getNombre().trim().isEmpty()) {
            String nombreClean = datosNuevos.getNombre().trim().replaceAll("\\s+", " ");
            
            if (!nombreClean.equalsIgnoreCase(habitacion.getNombre())) {
                Habitacion duplicado = habitacionRepository.findByNombreIgnoreCase(nombreClean);
                if (duplicado != null) {
                    throw new RuntimeException("Ya existe otra habitación con el nombre: '" + nombreClean + "'");
                }
            }
            habitacion.setNombre(nombreClean);
        }
        if (datosNuevos.getDescripcion() != null && !datosNuevos.getDescripcion().trim().isEmpty()) {
            String descClean = datosNuevos.getDescripcion().trim().replaceAll("\\s+", " ");
            habitacion.setDescripcion(descClean);
        }
        habitacion.setEsZonaSegura(datosNuevos.isEsZonaSegura());
        Habitacion editada = habitacionRepository.save(habitacion);
        return mapToDTO(editada);
    }

    public void eliminar(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo.");
        }
        if (!habitacionRepository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar. Habitación no encontrada con el ID: " + id);
        }
        habitacionRepository.deleteById(id);
    }

}
