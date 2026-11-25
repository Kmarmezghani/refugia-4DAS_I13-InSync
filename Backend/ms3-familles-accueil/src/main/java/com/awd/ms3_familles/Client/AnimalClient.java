package com.awd.ms3_familles.Client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "ms1-animaux", url = "http://localhost:8010")
public interface AnimalClient {

    @GetMapping("/api/animaux/{id}")
    Map<String, Object> getAnimalById(@PathVariable("id") Long id);

    @PatchMapping("/api/animaux/{id}/statut")
    void changerStatut(@PathVariable("id") Long id, @RequestBody Map<String, String> statutRequest);
}