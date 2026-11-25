package com.awd.veterinaire.entity;

public enum StatutTraitement {
    EN_COURS,
    TERMINE,
    SUSPENDU;

    public boolean peutPasserA(StatutTraitement cible) {
        return this == cible || switch (this) {
            case EN_COURS -> cible == SUSPENDU || cible == TERMINE;
            case SUSPENDU -> cible == EN_COURS || cible == TERMINE;
            case TERMINE -> false;
        };
    }
}
