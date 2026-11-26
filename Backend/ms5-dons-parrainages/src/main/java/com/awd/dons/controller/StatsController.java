package com.awd.dons.controller;

import com.awd.dons.dto.StatsDonsResponse;
import com.awd.dons.service.StatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
@Tag(name = "Statistiques")
public class StatsController {

    private final StatsService statsService;

    @GetMapping("/dons")
    @Operation(summary = "Total collecté sur une période et animaux les plus soutenus")
    public StatsDonsResponse dons(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return statsService.statistiques(debut, fin);
    }
}
