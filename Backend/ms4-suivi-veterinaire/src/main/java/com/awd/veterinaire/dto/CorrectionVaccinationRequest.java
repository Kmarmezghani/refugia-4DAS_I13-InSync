package com.awd.veterinaire.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
public class CorrectionVaccinationRequest {

    @Pattern(regexp = "(?s).*\\S.*")
    @Setter(onMethod_ = @JsonSetter(nulls = Nulls.FAIL))
    private String vaccin;

    @Setter(onMethod_ = @JsonSetter(nulls = Nulls.FAIL))
    private LocalDate dateVaccination;

    private LocalDate dateRappel;

    @JsonIgnore
    private boolean dateRappelModifiee;

    @JsonSetter
    public void setDateRappel(LocalDate dateRappel) {
        this.dateRappel = dateRappel;
        dateRappelModifiee = true;
    }

    @AssertTrue
    @JsonIgnore
    public boolean isCorrectionRenseignee() {
        return vaccin != null || dateVaccination != null || dateRappelModifiee;
    }
}
