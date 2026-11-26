package com.awd.dons.mapper;

import com.awd.dons.dto.DonResponse;
import com.awd.dons.entity.Don;
import com.awd.dons.entity.Paiement;
import com.awd.dons.entity.Recu;

public final class DonMapper {

    private DonMapper() {
    }

    public static DonResponse toResponse(Don d, Paiement paiement, Recu recu) {
        return new DonResponse(
                d.getId(),
                d.getDonateur().getId(),
                d.getDonateur().getNom(),
                d.getAnimalId(),
                d.getMontant(),
                d.getStatut(),
                d.getDateDon(),
                d.getMessage(),
                paiement != null ? paiement.getId() : null,
                recu != null ? recu.getNumero() : null);
    }
}
