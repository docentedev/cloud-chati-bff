package com.docente.proyectoejemplo.reports;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

@Component
public class ReportEventPublisher {

    /**
     * Valor de "type" en el cuerpo del mensaje. La suscripción SNS -> SQS filtra por
     * FilterPolicy {"type":[...]} con scope MessageBody, así que debe estar en la lista.
     */
    static final String MESSAGE_TYPE = "report.requested";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final SnsClient snsClient;
    private final String topicArn;

    public ReportEventPublisher(SnsClient snsClient, @Value("${aws.sns.topic-arn}") String topicArn) {
        this.snsClient = snsClient;
        this.topicArn = topicArn;
    }

    /** Publica la solicitud de informe en el topic SNS con el formato {type, body}. Retorna el messageId de SNS. */
    public String publishRequested(String requestId, String requester, String requesterId,
                                   String reportType, Map<String, Object> params, Instant requestedAt) {
        try {
            Map<String, Object> body = new LinkedHashMap<>();
            body.put("requestId", requestId);
            body.put("requester", requester);
            body.put("requesterId", requesterId);
            body.put("reportType", reportType);
            body.put("params", params == null ? Map.of() : params);
            body.put("requestedAt", requestedAt.toString());

            String message = OBJECT_MAPPER.writeValueAsString(Map.of("type", MESSAGE_TYPE, "body", body));

            return snsClient.publish(PublishRequest.builder()
                            .topicArn(topicArn)
                            .message(message)
                            .build())
                    .messageId();
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo publicar en SNS", e);
        }
    }
}
