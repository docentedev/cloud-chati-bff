package com.docente.proyectoejemplo.reports;

import com.docente.proyectoejemplo.reports.dto.ReportRequestDto;
import com.docente.proyectoejemplo.reports.dto.ReportResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PreAuthorize("hasRole('Admin')")
    @PostMapping
    public ResponseEntity<ReportResponseDto> generate(@AuthenticationPrincipal Jwt jwt,
                                                      @RequestBody ReportRequestDto request) {
        // 202: aceptado. SNS report-events -> report-request-recorder guarda la solicitud; -> report-generator genera el informe
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(reportService.request(jwt, request));
    }
}
