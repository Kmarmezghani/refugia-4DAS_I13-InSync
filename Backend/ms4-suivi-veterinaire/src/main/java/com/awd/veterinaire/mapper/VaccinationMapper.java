package com.awd.veterinaire.mapper;

import com.awd.veterinaire.dto.VaccinationResponse;
import com.awd.veterinaire.entity.Vaccination;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class VaccinationMapper {

    public VaccinationResponse versResponse(Vaccination vaccination, LocalDate aujourdhui) {
        return new VaccinationResponse(
                vaccination.getId(),
                vaccination.getDossier().getId(),
                vaccination.getVaccin(),
                vaccination.getDateVaccination(),
                vaccination.getDateRappel(),
                vaccination.isRappelPublie(),
                vaccination.calculerStatut(aujourdhui)
        );
    }
}
