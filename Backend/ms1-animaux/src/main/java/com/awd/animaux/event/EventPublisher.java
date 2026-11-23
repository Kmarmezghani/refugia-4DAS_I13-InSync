package com.awd.animaux.event;

/** Envoi d'un événement. Étape 1 : simple log ; étape 2 : RabbitMQ (exchange refuge.events). */
public interface EventPublisher {

    void publier(EvenementMetier evenement);
}
