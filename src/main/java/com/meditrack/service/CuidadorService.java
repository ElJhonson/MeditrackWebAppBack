package com.meditrack.service;

import com.meditrack.dto.cuidador.RequestCuidadorDto;
import com.meditrack.dto.cuidador.ResponseCuidadorDto;
import com.meditrack.mapper.CuidadorMapper;
import com.meditrack.model.Cuidador;
import com.meditrack.model.User;
import com.meditrack.repository.CuidadorRepository;
import com.meditrack.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CuidadorService {

    private final CuidadorRepository cuidadorRepository;
    private final UserRepository userRepo;
    private final BCryptPasswordEncoder encoder;


    public CuidadorService(CuidadorRepository cuidadorRepository, UserRepository userRepo) {
        this.cuidadorRepository = cuidadorRepository;
        this.userRepo = userRepo;
        this.encoder = new BCryptPasswordEncoder(12);
    }

    public ResponseCuidadorDto registrar(RequestCuidadorDto dto) {
        Optional<User> existente = userRepo.findByEmail(dto.getEmail());
        if (existente.isPresent()) {
            throw new IllegalStateException("El correo ya está registrado");
        }

        Cuidador cuidador = CuidadorMapper.toEntity(dto);

        Cuidador guardado = cuidadorRepository.save(cuidador);

        return CuidadorMapper.toResponse(guardado);
    }



}

