package com.meditrack.service;

import com.meditrack.model.User;
import com.meditrack.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
@Service
public class UserService {

    private final AuthenticationManager authManager;
    private final JWTService jwtService;
    private final UserRepository userRepository;

    public UserService(AuthenticationManager authManager,
                       JWTService jwtService,
                       UserRepository userRepository) {
        this.authManager = authManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    public Map<String, String> acceder(User user) {
        Authentication authentication =
                authManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                user.getPhoneNumber(),
                                user.getPassword()
                        )
                );

        if (authentication.isAuthenticated()) {
            User userBD = userRepository.findByPhoneNumber(user.getPhoneNumber())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            String accessToken = jwtService.generateToken(userBD);
            String refreshToken = jwtService.generateRefreshToken(userBD);
            return Map.of(
                    "accessToken", accessToken,
                    "refreshToken", refreshToken
            );
        }

        throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Credenciales incorrectas"
        );
    }

    public String refreshAccessToken(String refreshToken) {
        try {
            String telefono = jwtService.extractPhoneNumber(refreshToken);
            User user = userRepository.findByPhoneNumber(telefono)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.UNAUTHORIZED,
                            "Usuario no encontrado"
                    ));

            if (!jwtService.validateToken(refreshToken)) {
                throw new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Refresh token inválido o expirado"
                );
            }

            return jwtService.generateToken(user);

        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Error al refrescar token"
            );
        }
    }
}