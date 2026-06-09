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

    public Eventos actualizarInstancia(Long id, Eventos datosNuevos) {
        if (id == null || datosNuevos == null) {
            throw new IllegalArgumentException("El ID o los datos de actualización no pueden ser nulos.");
        }
        if (datosNuevos.getCategoria() == null || datosNuevos.getCategoria().trim().isEmpty()) {
            throw new IllegalArgumentException("La categoría es obligatoria para una actualización completa (PUT).");
        }
        Eventos existente = eventosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se puede actualizar. La categoría no existe con el ID: " + id));
        String categoriaSanitizada = datosNuevos.getCategoria().trim().replaceAll("\\s+", " ");
        if (!existente.getCategoria().equalsIgnoreCase(categoriaSanitizada)) {
            Eventos duplicado = eventosRepository.findByCategoriaIgnoreCase(categoriaSanitizada);
            if (duplicado != null) {
                throw new RuntimeException("Ya existe otra categoría con el nombre: '" + categoriaSanitizada + "'");
            }
        }
        existente.setCategoria(categoriaSanitizada);
        return eventosRepository.save(existente);
    }

    public Eventos editar(Long id, Eventos datosNuevos) {
        if (id == null || datosNuevos == null) {
            throw new IllegalArgumentException("El ID o los datos modificados no pueden ser nulos.");
        }

        Eventos existente = eventosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se puede editar. La categoría no existe con el ID: " + id));

        if (datosNuevos.getCategoria() != null && !datosNuevos.getCategoria().trim().isEmpty()) {
            String categoriaSanitizada = datosNuevos.getCategoria().trim().replaceAll("\\s+", " ");
            
            if (!existente.getCategoria().equalsIgnoreCase(categoriaSanitizada)) {
                Eventos duplicado = eventosRepository.findByCategoriaIgnoreCase(categoriaSanitizada);
                if (duplicado != null) {
                    throw new RuntimeException("Ya existe otra categoría con el nombre: '" + categoriaSanitizada + "'");
                }
            }
            existente.setCategoria(categoriaSanitizada);
        }

        return eventosRepository.save(existente);
    }

    public void eliminar(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID de la categoría de evento no puede ser nulo.");
        }
        if (!eventosRepository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar. La categoría de evento especificada no existe con el ID: " + id);
        }
        eventosRepository.deleteById(id);
    }

}
