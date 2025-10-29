package com.meditrack.repository;

import com.meditrack.model.Cuidador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CuidadorRepository extends JpaRepository<Cuidador, Long> {
    Optional<Cuidador> findByCodigoVinculacion(String codigoVinculacion);
}
