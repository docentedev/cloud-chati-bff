package com.docente.proyectoejemplo.reports;

import com.docente.proyectoejemplo.reports.dto.ReportRequestDto;
import com.docente.proyectoejemplo.reports.dto.ReportResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.UUID;

@Service
public class ReportService {
    private final ReportEventPublisher reportEventPublisher;

    public ReportService(ReportEventPublisher reportEventPublisher) {
        this.reportEventPublisher = reportEventPublisher;
    }

    public ReportResponseDto request(Jwt jwt, ReportRequestDto request) {
        if (request == null || request.type() == null || request.type().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El campo 'type' del informe es obligatorio");
        }

        // El id se genera acá para devolverlo de inmediato (la fila aún no existe:
        // la inserta la Lambda miReceptor al procesar el mensaje).
        String requestId = UUID.randomUUID().toString();
        String requester = resolveRequester(jwt);
        Instant requestedAt = Instant.now();

        reportEventPublisher.publishRequested(requestId, requester, jwt.getSubject(),
                request.type().trim(), request.params(), requestedAt);

        return new ReportResponseDto(requestId, request.type().trim(), requester, requestedAt, "PENDING");
    }

    /** email -> cognito:username -> sub, el primero que venga en el JWT. */
    private String resolveRequester(Jwt jwt) {
        String email = jwt.getClaimAsString("email");
        if (email != null && !email.isBlank()) {
            return email;
        }
        String username = jwt.getClaimAsString("cognito:username");
        if (username != null && !username.isBlank()) {
            return username;
        }
        return jwt.getSubject();
    }
}
