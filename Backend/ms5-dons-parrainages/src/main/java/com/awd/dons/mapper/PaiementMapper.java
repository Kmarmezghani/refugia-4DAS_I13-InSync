package com.awd.dons.mapper;

import com.awd.dons.dto.PaiementResponse;
import com.awd.dons.dto.RecuResponse;
import com.awd.dons.entity.Donateur;
import com.awd.dons.entity.Paiement;
import com.awd.dons.entity.Recu;

public final class PaiementMapper {

    private PaiementMapper() {
    }

    public static PaiementResponse toResponse(Paiement p, Recu recu) {
        return new PaiementResponse(
                p.getId(),
                p.getDon() != null ? p.getDon().getId() : null,
                p.getParrainage() != null ? p.getParrainage().getId() : null,
                p.getMontant(),
                p.getDatePaiement(),
                p.getModePaiement(),
                p.getStatut(),
                recu != null ? recu.getNumero() : null);
    }

    public static RecuResponse toResponse(Recu r) {
        Paiement p = r.getPaiement();
        Donateur donateur = p.getDon() != null ? p.getDon().getDonateur() : p.getParrainage().getDonateur();
        return new RecuResponse(
                r.getId(),
                r.getNumero(),
                r.getDateEmission(),
                r.getMontant(),
                p.getId(),
                donateur.getNom(),
                donateur.getEmail());
    }
}