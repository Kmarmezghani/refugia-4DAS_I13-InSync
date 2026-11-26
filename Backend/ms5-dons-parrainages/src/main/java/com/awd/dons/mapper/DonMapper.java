package com.awd.dons.mapper;

import com.awd.dons.dto.DonResponse;
import com.awd.dons.entity.Don;

public final class DonMapper {

    private DonMapper() {
    }

    public static DonResponse toResponse(Don d) {
        return new DonResponse(
                d.getId(),
                d.getDonateur().getId(),
                d.getDonateur().getNom(),
                d.getAnimalId(),
                d.getMontant(),
                d.getStatut(),
                d.getDateDon(),
                d.getMessage());
    }
}