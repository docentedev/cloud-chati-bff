package com.docente.proyectoejemplo.reports.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;

/**
 * Solicitud de informe. La fila la inserta la Lambda miReceptor al procesar el mensaje de SNS;
 * esta entidad existe para que ddl-auto=update cree la tabla y para poder consultarla desde el BFF.
 */
@Entity
@Table(name = "report_requests")
public class ReportRequest {

    @Id
    private String id;
    private String requester;
    private String requesterId;
    private String type;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String params;

    private Instant requestedAt;

    // Esto lo requiere JPA
    protected ReportRequest() {
    }

    public String getId() {
        return id;
    }

    public String getRequester() {
        return requester;
    }

    public String getRequesterId() {
        return requesterId;
    }

    public String getType() {
        return type;
    }

    public String getParams() {
        return params;
    }

    public Instant getRequestedAt() {
        return requestedAt;
    }
}
