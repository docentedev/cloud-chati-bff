package com.docente.proyectoejemplo.reports.dto;

import java.util.Map;

/** Body de POST /api/reports: tipo de informe y sus parámetros libres (rango de fechas, filtros, etc.). */
public record ReportRequestDto(String type, Map<String, Object> params) {
}
