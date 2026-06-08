package com.Mansion.HabitacionesMC.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Mansion.HabitacionesMC.Model.Eventos;
import com.Mansion.HabitacionesMC.Repository.EventosRepository;

@Service
public class EventosService {

    @Autowired
    private EventosRepository eventosRepository;

    public List<Eventos> listarCategorias() {
        return eventosRepository.findAll();
    }

    public Eventos obtenerPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID de la instancia no puede ser nulo.");
        }
        return eventosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Instancia de evento no encontrada"));
    }

    public void eliminar(Long id) {
        eventosRepository.deleteById(id);
    }


}
