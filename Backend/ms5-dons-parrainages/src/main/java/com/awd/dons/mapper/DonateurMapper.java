package com.awd.dons.mapper;


import com.awd.dons.dto.DonateurRequest;
import com.awd.dons.dto.DonateurResponse;
import com.awd.dons.entity.Donateur;

public final class DonateurMapper {

    private DonateurMapper() {
    }

    public static DonateurResponse toResponse(Donateur d) {
        return new DonateurResponse(
                d.getId(),
                d.getNom(),
                d.getEmail(),
                d.getTelephone(),
                d.getDateInscription());
    }

    public static void copier(DonateurRequest req, Donateur d) {
        d.setNom(req.nom().trim());                    // trim() enlève les espaces au début et à la fin
        d.setEmail(req.email().trim().toLowerCase());  // e-mails toujours en minuscules
        d.setTelephone(req.telephone());
    }
}