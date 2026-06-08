package com.Mansion.HabitacionesMC.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Mansion.HabitacionesMC.Model.Objetos;

@Repository
public interface ObjetosRepository extends JpaRepository<Objetos, Long> {
    
}
