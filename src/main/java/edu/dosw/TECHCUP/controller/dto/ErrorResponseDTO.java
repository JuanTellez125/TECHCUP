package edu.dosw.TECHCUP.controller.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class ErrorResponseDTO {
    private int status;
    private String error;
    private String mensaje;
    private LocalDateTime timestamp;

    public ErrorResponseDTO(int status, String error, String mensaje) {
        this.status = status;
        this.error = error;
        this.mensaje = mensaje;
        this.timestamp = LocalDateTime.now();
    }

}