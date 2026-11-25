package com.awd.veterinaire.mapper;

import com.awd.veterinaire.dto.ConsultationRequest;
import com.awd.veterinaire.dto.ConsultationResponse;
import com.awd.veterinaire.entity.Consultation;
import com.awd.veterinaire.entity.DossierMedical;
import org.springframework.stereotype.Component;

@Component
public class ConsultationMapper {

    public Consultation versEntite(ConsultationRequest request, DossierMedical dossier) {
        return new Consultation(
                dossier,
                request.dateConsultation(),
                request.motif(),
                request.diagnostic(),
                request.poids(),
                request.temperature(),
                request.remarques()
        );
    }

    public ConsultationResponse versResponse(Consultation consultation) {
        return new ConsultationResponse(
                consultation.getId(),
                consultation.getDossier().getId(),
                consultation.getDateConsultation(),
                consultation.getMotif(),
                consultation.getDiagnostic(),
                consultation.getPoids(),
                consultation.getTemperature(),
                consultation.getRemarques()
        );
    }
}
