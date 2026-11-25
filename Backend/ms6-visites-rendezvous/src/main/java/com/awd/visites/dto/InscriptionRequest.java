package com.awd.visites.dto;

import jakarta.validation.constraints.NotNull;

public record InscriptionRequest(
        @NotNull Long participantId
) {}