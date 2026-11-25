package com.awd.veterinaire.mapper;

import com.awd.veterinaire.dto.DossierMedicalResponse;
import com.awd.veterinaire.entity.DossierMedical;
import org.springframework.stereotype.Component;

@Component
public class DossierMedicalMapper {

    public DossierMedicalResponse versResponse(DossierMedical dossier) {
        return new DossierMedicalResponse(
                dossier.getId(),
                dossier.getAnimalId(),
                dossier.getNumeroDossier(),
                dossier.getDateOuverture(),
                dossier.getNotesGenerales()
        );
    }
}
