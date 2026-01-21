    package com.meditrack.repository;
    
    import com.meditrack.model.Cuidador;
    import com.meditrack.model.Paciente;
    import com.meditrack.model.User;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.stereotype.Repository;
    
    import java.util.List;
    import java.util.Optional;
    
    @Repository
    public interface CuidadorRepository extends JpaRepository<Cuidador, Long> {
        Optional<Cuidador> findByCodigoVinculacion(String codigoVinculacion);
        Optional<Cuidador> findByUserPhoneNumber(String phoneNumber);
        Optional<Cuidador> findByUserId(Long userId);
    }


