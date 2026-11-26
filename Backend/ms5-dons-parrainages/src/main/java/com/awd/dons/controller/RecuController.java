package com.awd.dons.controller;

import com.awd.dons.dto.RecuResponse;
import com.awd.dons.service.PaiementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/paiements")
@RequiredArgsConstructor
@Tag(name = "Reçus")
public class RecuController {

    private final PaiementService paiementService;

    @GetMapping("/{paiementId}/recu")
    @Operation(summary = "Récupérer le reçu d'un paiement")
    public RecuResponse getRecu(@PathVariable Long paiementId) {
        return paiementService.getRecuDuPaiement(paiementId);
    }
}