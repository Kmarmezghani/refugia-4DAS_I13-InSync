package com.awd.animaux.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/** Implémentation provisoire : écrit l'événement dans les logs. Remplacée par RabbitMQ à l'étape 2. */
@Component
public class LoggingEventPublisher implements EventPublisher {

    private static final Logger log = LoggerFactory.getLogger(LoggingEventPublisher.class);

    @Override
    public void publier(EvenementMetier evenement) {
        log.info("[événement] {} ({}) : {}", evenement.envelope().eventType(), evenement.routingKey(),
                evenement.envelope());
    }
}
