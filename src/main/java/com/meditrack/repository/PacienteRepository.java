package com.meditrack.repository;

import com.meditrack.model.Paciente;
import com.meditrack.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    Optional<Paciente> findByUser(User user);
    @Query("""
    SELECT p FROM Paciente p JOIN FETCH p.user LEFT JOIN FETCH p.cuidador c  LEFT JOIN FETCH c.user WHERE p.user.id = :userId""")
    Optional<Paciente> findByUserId(@Param("userId") Long userId);

}
