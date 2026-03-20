package com.meditrack.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Entity
    @Table(name = "users")
    public class User {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        @Column(name = "nombre_completo")
        private String name;
        @Column(unique = true, nullable = false, length = 10, name = "telefono")
        private String phoneNumber;
        @Column(name = "contrasena")
        private String password;
        @Enumerated(EnumType.STRING)
        private Rol rol;
        @Column(name = "estado")
        @Enumerated(EnumType.STRING)
        private Estado state = Estado.ACTIVO;


        @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
        @JsonIgnore
        private Paciente paciente;

        @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
        @JsonIgnore
        private Cuidador cuidador;

    }
