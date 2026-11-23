package com.awd.animaux.event;

/** Événement à publier sur l'exchange refuge.events avec sa clé de routage. */
public record EvenementMetier(String routingKey, EventEnvelope envelope) {
}
