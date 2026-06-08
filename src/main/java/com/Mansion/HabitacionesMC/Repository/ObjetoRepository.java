package com.Mansion.HabitacionesMC.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Mansion.HabitacionesMC.Model.Objeto;

@Repository
public interface ObjetoRepository extends JpaRepository<Objeto, Long> {
    
}
