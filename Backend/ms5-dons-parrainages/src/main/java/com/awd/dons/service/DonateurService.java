package com.awd.dons.service;

import com.awd.dons.dto.DonateurRequest;
import com.awd.dons.dto.DonateurResponse;
import com.awd.dons.entity.Donateur;
import com.awd.dons.mapper.DonateurMapper;
import com.awd.dons.repository.DonateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DonateurService {

    private final DonateurRepository donateurRepository;

    /** CREATE */
    @Transactional
    public DonateurResponse creer(DonateurRequest req) {
        // Règle métier : e-mail unique -> 409 Conflict
        if (donateurRepository.existsByEmailIgnoreCase(req.email().trim())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Un donateur existe déjà avec l'e-mail " + req.email());
        }
        Donateur d = new Donateur();
        DonateurMapper.copier(req, d);
        d.setDateInscription(LocalDate.now());          // date décidée par le serveur
        Donateur enregistre = donateurRepository.save(d); // INSERT
        return DonateurMapper.toResponse(enregistre);
    }

    /** READ (tous) */
    @Transactional(readOnly = true)
    public List<DonateurResponse> lister() {
        return donateurRepository.findAll()
                .stream()
                .map(DonateurMapper::toResponse)
                .toList();
    }

    /** READ (un seul) */
    @Transactional(readOnly = true)
    public DonateurResponse getById(Long id) {
        return DonateurMapper.toResponse(getEntity(id));
    }

    /** UPDATE */
    @Transactional
    public DonateurResponse modifier(Long id, DonateurRequest req) {
        Donateur d = getEntity(id);
        String nouvelEmail = req.email().trim();
        // Si l'e-mail change, il ne doit pas déjà appartenir à quelqu'un d'autre
        if (!d.getEmail().equalsIgnoreCase(nouvelEmail)
                && donateurRepository.existsByEmailIgnoreCase(nouvelEmail)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Un autre donateur utilise déjà l'e-mail " + req.email());
        }
        DonateurMapper.copier(req, d);
        return DonateurMapper.toResponse(donateurRepository.save(d)); // UPDATE
    }

    /** DELETE */
    @Transactional
    public void supprimer(Long id) {
        Donateur d = getEntity(id);
        donateurRepository.delete(d);

    }

    public Donateur getEntity(Long id) {
        return donateurRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Donateur " + id + " introuvable"));
    }
}