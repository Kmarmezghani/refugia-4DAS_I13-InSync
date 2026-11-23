package com.awd.animaux.event;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Les services émettent leurs événements dans la transaction ; ils ne partent qu'après le commit.
 * Ainsi, un événement n'est jamais publié pour une modification annulée.
 */
@Component
public class EventRelay {

    private final EventPublisher publisher;

    public EventRelay(EventPublisher publisher) {
        this.publisher = publisher;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void apresCommit(EvenementMetier evenement) {
        publisher.publier(evenement);
    }
}
