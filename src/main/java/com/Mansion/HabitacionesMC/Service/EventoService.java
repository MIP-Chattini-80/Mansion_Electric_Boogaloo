package com.Mansion.HabitacionesMC.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Mansion.HabitacionesMC.DTO.EventoDTO;
import com.Mansion.HabitacionesMC.Model.Evento;
import com.Mansion.HabitacionesMC.Repository.EventoRepository;


@Transactional
@Service
public class EventoService {

    @Autowired
    private EventoRepository eventoRepository;

    public List<EventoDTO> listarEventosDTO() {
        return eventoRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Evento buscarPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo.");
        }
        return eventoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ID "+ id + " no está registrado"));
    }

    public EventoDTO completarEvento(Long id) {
        return eventoRepository.findById(id).map(evento -> {
            evento.setCompletado(true);
            return mapToDTO(eventoRepository.save(evento));
        }).orElse(null);
    } /* Como dice el nombre: este apartado trata de cambiar el estado de un evento a "Completado" dentro de la base de datos modificando el boolean "isCompletado" */


    private EventoDTO mapToDTO(Evento evento) {
        EventoDTO dto = new EventoDTO();
        dto.setIdEvento(evento.getIdEvento());
        dto.setDescripcionEspecifica(evento.getDescripcionEspecifica());
        dto.setCompletado(evento.isCompletado()); // Asignación directa pura
        return dto;
    }

    public Evento guardarEvento(Evento evento) {
        if (evento == null) {
            throw new IllegalArgumentException("El objeto Evento no puede ser nulo.");
        }
        if (evento.getTipoEvento() == null || evento.getTipoEvento().getIdEventos() == null) {
            throw new IllegalArgumentException("Debe tener un tipo o categoría de evento válida (idEventos).");
        }
        if (evento.getHabitacion() == null) {
            throw new IllegalArgumentException("Debe indicar obligatoriamente la habitación donde ocurre.");
        }
        if (evento.getDescripcionEspecifica() == null || evento.getDescripcionEspecifica().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción específica es necesaria para dar contexto.");
        }
        String descClean = evento.getDescripcionEspecifica().trim().replaceAll("\\s+", " "); /* Esto es para limpiar espacios en blanco */
        evento.setDescripcionEspecifica(descClean);
        return eventoRepository.save(evento);
    }

    public Evento actualizarEvento(Long id, Evento eventoDetalles) {
        if (id == null || eventoDetalles == null) {
            throw new IllegalArgumentException("El ID o los datos de actualización no pueden ser nulos.");
        }
        Evento eventoExistente = eventoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se puede actualizar. El evento físico no existe con el ID: " + id));
        if (eventoDetalles.getTipoEvento() != null) {
            eventoExistente.setTipoEvento(eventoDetalles.getTipoEvento());
        }
        if (eventoDetalles.getHabitacion() != null) {
            eventoExistente.setHabitacion(eventoDetalles.getHabitacion());
        }
        if (eventoDetalles.getDescripcionEspecifica() != null && !eventoDetalles.getDescripcionEspecifica().trim().isEmpty()) {
            String descSanitizada = eventoDetalles.getDescripcionEspecifica().trim().replaceAll("\\s+", " "); /* Elimina espacios extra en los extremos y reduce múltiples espacios intermedios a uno solo */
            eventoExistente.setDescripcionEspecifica(descSanitizada);
        }
        eventoExistente.setCompletado(eventoDetalles.isCompletado()); /*Actualizar el estado*/
        return eventoRepository.save(eventoExistente);
    }

    public Evento editarEvento(Long id, Evento datosNuevos) {
        if (id == null || datosNuevos == null) {
            throw new IllegalArgumentException("El ID o los datos para editar no pueden ser nulos.");
        }
        Evento existente = eventoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe el evento con el ID: " + id));
        if (datosNuevos.getTipoEvento() != null) {
            existente.setTipoEvento(datosNuevos.getTipoEvento());
        }
        if (datosNuevos.getHabitacion() != null) {
            existente.setHabitacion(datosNuevos.getHabitacion());
        }
        if (datosNuevos.getDescripcionEspecifica() != null && !datosNuevos.getDescripcionEspecifica().trim().isEmpty()) {
            String descLimpia = datosNuevos.getDescripcionEspecifica().trim().replaceAll("\\s+", " ");
            existente.setDescripcionEspecifica(descLimpia);
        }
        existente.setCompletado(datosNuevos.isCompletado());
        return eventoRepository.save(existente);
    } /* METODO PARA EL PATCH */

    public void eliminar(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo.");
        }
        if (!eventoRepository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar. Evento no encontrado");
        }
        eventoRepository.deleteById(id);
    }

}