package com.Mansion.HabitacionesMC.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Mansion.HabitacionesMC.DTO.ObjetoDTO;
import com.Mansion.HabitacionesMC.Model.Objeto;
import com.Mansion.HabitacionesMC.Repository.ObjetoRepository;


@Transactional
@Service
public class ObjetoService {

    @Autowired
    private ObjetoRepository objetoRepository;

    private ObjetoDTO mapToDTO(Objeto modelo) {
        ObjetoDTO dto = new ObjetoDTO();
        dto.setIdObjeto(modelo.getIdObjeto());
        dto.setNombre(modelo.getNombre());
        dto.setDescripcion(modelo.getDescripcion());
        dto.setTipoObjeto(modelo.getTipoObjeto());
        dto.setValorBase(modelo.getValorBase()); 
        return dto;
    }

    private void mapearEntidad(Objeto objeto, ObjetoDTO dto) {
        if (dto.getNombre() != null && !dto.getNombre().trim().isEmpty()) {
            objeto.setNombre(dto.getNombre().trim().replaceAll("\\s+", " "));
        }
        if (dto.getDescripcion() != null && !dto.getDescripcion().trim().isEmpty()) {
            objeto.setDescripcion(dto.getDescripcion().trim().replaceAll("\\s+", " "));
        }
        if (dto.getTipoObjeto() != null && !dto.getTipoObjeto().trim().isEmpty()) {
            objeto.setTipoObjeto(dto.getTipoObjeto().trim());
        }
        if (dto.getValorBase() != null) {
            objeto.setValorBase(dto.getValorBase());
        }
    }

    public List<ObjetoDTO> listarTodo() {
        return objetoRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public ObjetoDTO obtenerPorId(Long id) {
        if (id == null) throw new IllegalArgumentException("El ID del objeto no puede ser nulo.");
        return objetoRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Objeto físico no encontrado con el ID: " + id));
    }

    public List<ObjetoDTO> buscarPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("Debes colocar un nombre para buscar.");
        }
        return objetoRepository.findByNombreContainingIgnoreCase(nombre.trim()).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<ObjetoDTO> buscarPorTipoObjeto(String tipoObjeto) {
        if (tipoObjeto == null || tipoObjeto.trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de objeto no puede estar vacío.");
        }
        return objetoRepository.findByTipoObjeto(tipoObjeto.trim()).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public ObjetoDTO guardarObjeto(Objeto objeto) {
        if (objeto == null) throw new IllegalArgumentException("Los datos del objeto no pueden ser nulos.");
        if (objeto.getNombre() == null || objeto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del objeto es obligatorio.");
        }
        if (objeto.getDescripcion() == null || objeto.getDescripcion().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción es obligatoria.");
        }
        if (objeto.getValorBase() == null || objeto.getValorBase() < 0) {
            throw new IllegalArgumentException("El valor base debe ser definido y no puede ser negativo.");
        }
        objeto.setNombre(objeto.getNombre().trim().replaceAll("\\s+", " "));
        objeto.setDescripcion(objeto.getDescripcion().trim().replaceAll("\\s+", " "));
        if (objeto.getTipoObjeto() != null) {
            objeto.setTipoObjeto(objeto.getTipoObjeto().trim());
        }
        Objeto guardado = objetoRepository.save(objeto);
        return mapToDTO(guardado);
    }

    public ObjetoDTO actualizarObjeto(Long id, Objeto datosNuevos) {
        if (id == null || datosNuevos == null) {
            throw new IllegalArgumentException("El ID o los datos para actualizar no pueden ser nulos.");
        }
        if (datosNuevos.getNombre() == null || datosNuevos.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del objeto es obligatorio para un reemplazo completo (PUT).");
        }
        if (datosNuevos.getDescripcion() == null || datosNuevos.getDescripcion().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción es obligatoria para un reemplazo completo (PUT).");
        }
        if (datosNuevos.getValorBase() == null) {
            throw new IllegalArgumentException("El valor base es obligatorio para un reemplazo completo (PUT).");
        }
        Objeto existente = objetoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se puede actualizar. El objeto no existe con el ID: " + id));
        existente.setNombre(datosNuevos.getNombre().trim().replaceAll("\\s+", " "));
        existente.setDescripcion(datosNuevos.getDescripcion().trim().replaceAll("\\s+", " "));
        existente.setTipoObjeto(datosNuevos.getTipoObjeto());
        existente.setValorBase(datosNuevos.getValorBase());
        Objeto actualizado = objetoRepository.save(existente);
        return mapToDTO(actualizado);
    }

    public ObjetoDTO editarObjeto(Long id, ObjetoDTO datosNuevos) {
        if (id == null || datosNuevos == null) {
            throw new IllegalArgumentException("El ID o los datos para editar no pueden ser nulos.");
        }
        Objeto existente = objetoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se puede editar. El objeto no existe con el ID: " + id));
        mapearEntidad(existente, datosNuevos);      
        Objeto actualizado = objetoRepository.save(existente);
        return mapToDTO(actualizado);
    }

    public void eliminarObjeto(Long id) {
        if (id == null) throw new IllegalArgumentException("El ID no puede ser nulo.");
        if (!objetoRepository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar. El objeto no existe en la base de datos.");
        }
        objetoRepository.deleteById(id);
    }

}
