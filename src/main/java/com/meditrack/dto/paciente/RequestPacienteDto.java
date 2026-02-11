package com.meditrack.dto.paciente;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class RequestPacienteDto {
        private String name;
        private Integer edad;
        private String phoneNumber;
        private String password;
    }
