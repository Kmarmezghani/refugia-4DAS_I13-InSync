package com.awd.dons.mapper;

import com.awd.dons.dto.ParrainageResponse;
import com.awd.dons.entity.Parrainage;

public final class ParrainageMapper {

    private ParrainageMapper() {
    }

    public static ParrainageResponse toResponse(Parrainage p) {
        return new ParrainageResponse(
                p.getId(),
                p.getDonateur().getId(),
                p.getDonateur().getNom(),
                p.getAnimalId(),
                p.getMontantMensuel(),
                p.getDateDebut(),
                p.getDateFin(),
                p.getStatut());
    }
}
