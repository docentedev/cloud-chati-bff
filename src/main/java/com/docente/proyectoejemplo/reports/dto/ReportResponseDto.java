package com.docente.proyectoejemplo.reports.dto;

import java.time.Instant;

public record ReportResponseDto(String requestId, String type, String requester, Instant requestedAt, String status) {
}
