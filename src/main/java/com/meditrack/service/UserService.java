package com.meditrack.service;

import com.meditrack.model.User;
import com.meditrack.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserService {

    private final AuthenticationManager authManager;
    private final JWTService jwtService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final UserDetailsService userDetailsService;

    public UserService(AuthenticationManager authManager,
                       JWTService jwtService,
                       UserRepository userRepository,
                       UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
        this.encoder = new BCryptPasswordEncoder(12);
        this.authManager = authManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    public Map<String, String> acceder(User user) {
        Authentication authentication =
                authManager.authenticate(new UsernamePasswordAuthenticationToken
                        (user.getPhoneNumber(), user.getPassword()));

        if(authentication.isAuthenticated()){
            User userBD = userRepository.findByPhoneNumber(user.getPhoneNumber())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            String accessToken = jwtService.generateToken(userBD);
            String refreshToken = jwtService.generateRefreshToken(userBD);
            return Map.of("accessToken", accessToken,
                    "refreshToken", refreshToken);
        }
        return Map.of("error", "failed");
    }


    public String refreshAccessToken(String refreshToken) {
        String correo = jwtService.extractPhoneNumber(refreshToken);
        User user = userRepository.findByPhoneNumber(correo).orElseThrow();
        UserDetails userDetails = userDetailsService.loadUserByUsername(correo);

        if (!jwtService.validateToken(refreshToken)) {
            throw new RuntimeException("Refresh token invalido o expirado");
        }

        return jwtService.generateToken(user);
    }
}
    