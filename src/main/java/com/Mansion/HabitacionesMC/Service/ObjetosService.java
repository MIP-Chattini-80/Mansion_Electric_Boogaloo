package com.Mansion.HabitacionesMC.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Mansion.HabitacionesMC.DTO.ObjetosDTO;
import com.Mansion.HabitacionesMC.Model.Objetos;
import com.Mansion.HabitacionesMC.Repository.ObjetosRepository;

@Transactional
@Service
public class ObjetosService {

    @Autowired
    private ObjetosRepository objetosRepository;

    private ObjetosDTO mapToDTO(Objetos modelo) {
        if (modelo == null) return null;
        return modelo.convertirADTO();
    }

    private void mapearCambiosEntidad(Objetos existente, ObjetosDTO dto) {
        if (dto.getEstado() != null && !dto.getEstado().trim().isEmpty()) {
            existente.setEstado(dto.getEstado().trim().replaceAll("\\s+", " "));
        }
        if (dto.getCantidad() != null) {
            existente.setCantidad(dto.getCantidad());
        }
    }

    public List<ObjetosDTO> listarTodo() {
        return objetosRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public ObjetosDTO obtenerPorId(Long id) {
        if (id == null) throw new IllegalArgumentException("El ID de la instancia no puede ser nulo.");
        return objetosRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Instancia física del objeto no encontrada con el ID: " + id));
    }

    public List<ObjetosDTO> buscarPorHabitacion(Long idHabitacion) {
        if (idHabitacion == null) {
            throw new IllegalArgumentException("El ID de la habitación no puede ser nulo.");
        }
        return objetosRepository.findByUbicacion_IdHabitacion(idHabitacion).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public ObjetosDTO guardar(Objetos objetos) {
        if (objetos == null) throw new IllegalArgumentException("Los datos del objeto no pueden ser nulos.");
        if (objetos.getObjetoBase() == null) {
            throw new IllegalArgumentException("La instancia debe estar vinculada a un objeto base del catálogo.");
        }
        if (objetos.getUbicacion() == null) {
            throw new IllegalArgumentException("La instancia debe estar ubicada en una habitación válida.");
        }
        if (objetos.getEstado() != null) {
            objetos.setEstado(objetos.getEstado().trim().replaceAll("\\s+", " ")); /* Sirve para limpiar de espacios blancos y no haya errores */
        }

        Objetos guardado = objetosRepository.save(objetos);
        return mapToDTO(guardado);
    }

    public ObjetosDTO actualizar(Long id, ObjetosDTO datosNuevos) {
        if (id == null || datosNuevos == null) {
            throw new IllegalArgumentException("El ID o los datos de actualización no pueden ser nulos.");
        }
        if (datosNuevos.getEstado() == null || datosNuevos.getEstado().trim().isEmpty()) {
            throw new IllegalArgumentException("El estado es obligatorio para una actualización completa (PUT).");
        }
        if (datosNuevos.getCantidad() == null || datosNuevos.getCantidad() < 0) {
            throw new IllegalArgumentException("La cantidad es requerida y no puede ser menor a 0 en PUT.");
        }
        Objetos existente = objetosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se puede actualizar. La instancia no existe con el ID: " + id));
        existente.setEstado(datosNuevos.getEstado().trim().replaceAll("\\s+", " "));
        existente.setCantidad(datosNuevos.getCantidad());

        Objetos actualizado = objetosRepository.save(existente);
        return mapToDTO(actualizado);
    }

    public ObjetosDTO editar(Long id, ObjetosDTO datosNuevos) {
        if (id == null || datosNuevos == null) {
            throw new IllegalArgumentException("El ID o los datos modificados no pueden ser nulos.");
        }
        Objetos existente = objetosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se puede editar. La instancia no existe con el ID: " + id));
        mapearCambiosEntidad(existente, datosNuevos);
        Objetos actualizado = objetosRepository.save(existente);
        return mapToDTO(actualizado);
    }

    public void eliminar(Long id) {
        if (id == null) throw new IllegalArgumentException("El ID de la instancia no puede ser nulo.");
        
        if (!objetosRepository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar. La instancia de objeto especificada no existe.");
        }
        objetosRepository.deleteById(id);
    }

}
