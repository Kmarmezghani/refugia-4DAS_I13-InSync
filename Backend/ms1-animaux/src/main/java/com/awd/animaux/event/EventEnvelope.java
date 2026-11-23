package com.awd.animaux.event;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

/** Enveloppe commune à tous les événements du projet (section 6.2). */
public record EventEnvelope(String eventId, String eventType, Instant occurredAt, Map<String, Object> data) {

    public static EventEnvelope of(String eventType, Map<String, Object> data) {
        return new EventEnvelope(UUID.randomUUID().toString(), eventType, Instant.now(), data);
    }
}
