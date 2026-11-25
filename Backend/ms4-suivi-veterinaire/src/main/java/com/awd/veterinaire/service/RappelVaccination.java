package com.awd.veterinaire.service;

import java.time.LocalDate;

public record RappelVaccination(Long animalId, String vaccin, LocalDate dateRappel) {
}
