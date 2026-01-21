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
        private long id;
        private String name;
        private String phoneNumber;
        private String password;
        @Enumerated(EnumType.STRING)
        private Rol rol;
        @Enumerated(EnumType.STRING)
        private Estado state = Estado.ACTIVO;


        @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
        @JsonIgnore
        private Paciente paciente;

        @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
        @JsonIgnore
        private Cuidador cuidador;

    }
