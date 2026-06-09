package com.Mansion.HabitacionesMC.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Mansion.HabitacionesMC.Model.Eventos;
import com.Mansion.HabitacionesMC.Repository.EventosRepository;


@Transactional
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

    public Eventos guardarInstancia(Eventos eventos) {
        if (eventos == null) {
            throw new IllegalArgumentException("El objeto Eventos no puede ser nulo.");
        }
        if (eventos.getCategoria() == null || eventos.getCategoria().trim().isEmpty()) {
            throw new IllegalArgumentException("La categoría es estrictamente obligatoria.");
        }
        String categoriaSanitizada = eventos.getCategoria().trim().replaceAll("\\s+", " ");
        Eventos existente = eventosRepository.findByCategoriaIgnoreCase(categoriaSanitizada);
        if (existente != null) {
            throw new RuntimeException("Ya existe un registro con la categoría: '" + categoriaSanitizada + "'");
        } /* Control de dublicados: evita que hayan duplicados de las instancias */
        eventos.setCategoria(categoriaSanitizada);
        return eventosRepository.save(eventos);
    }

    

    public void eliminar(Long id) {
        eventosRepository.deleteById(id);
    }


}
