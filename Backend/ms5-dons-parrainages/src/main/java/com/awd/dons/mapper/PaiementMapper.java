package com.awd.dons.mapper;

import com.awd.dons.dto.PaiementResponse;
import com.awd.dons.entity.Paiement;

public final class PaiementMapper {

    private PaiementMapper() {
    }

    public static PaiementResponse toResponse(Paiement p) {
        return new PaiementResponse(
                p.getId(),
                p.getDon() != null ? p.getDon().getId() : null,
                p.getParrainage() != null ? p.getParrainage().getId() : null,
                p.getMontant(),
                p.getDatePaiement(),
                p.getModePaiement(),
                p.getStatut());
    }
}