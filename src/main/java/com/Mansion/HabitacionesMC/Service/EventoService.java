package com.Mansion.HabitacionesMC.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Mansion.HabitacionesMC.DTO.EventoDTO;
import com.Mansion.HabitacionesMC.Model.Evento;
import com.Mansion.HabitacionesMC.Repository.EventoRepository;

import jakarta.transaction.Transactional;

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

    public Evento obtenerPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo.");
        }
        return eventoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ID "+ id + " no está registrado"));
    }

    public Evento guardarEvento(Evento evento) {
        return eventoRepository.save(evento);
    }

    public EventoDTO completarEvento(Long id) {
        return eventoRepository.findById(id).map(evento -> {
            evento.setCompletado(true);
            return mapToDTO(eventoRepository.save(evento));
        }).orElse(null);
    }

    private EventoDTO mapToDTO(Evento evento) {
        EventoDTO dto = new EventoDTO();
        dto.setIdEvento(evento.getIdEvento());
        dto.setDescripcionEspecifica(evento.getDescripcionEspecifica());
        dto.setCompletado(evento.isCompletado());
        return dto;

        
    }

}