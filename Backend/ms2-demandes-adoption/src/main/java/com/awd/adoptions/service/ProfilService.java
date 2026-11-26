package com.awd.adoptions.service;

import com.awd.adoptions.common.exception.ConflictException;
import com.awd.adoptions.common.exception.ResourceNotFoundException;
import com.awd.adoptions.dto.ProfilRequest;
import com.awd.adoptions.dto.ProfilResponse;
import com.awd.adoptions.dto.ProfilUpdateRequest;
import com.awd.adoptions.entity.ProfilCandidat;
import com.awd.adoptions.mapper.AdoptionMapper;
import com.awd.adoptions.repository.ProfilCandidatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfilService {

    private final ProfilCandidatRepository repository;
    private final AdoptionMapper mapper;

    public ProfilResponse creer(ProfilRequest request) {
        if (repository.existsByClientId(request.clientId())) {
            throw new ConflictException("Ce client a déjà un profil");
        }
        return mapper.toResponse(repository.save(mapper.toEntity(request)));
    }

    @Transactional(readOnly = true)
    public ProfilResponse trouver(Long id) {
        return mapper.toResponse(chercher(id));
    }

    @Transactional(readOnly = true)
    public ProfilResponse trouverParClient(String clientId) {
        return repository.findByClientId(clientId)
                .map(mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Aucun profil pour le client " + clientId));
    }

    public ProfilResponse modifier(Long id, ProfilUpdateRequest request) {
        ProfilCandidat profil = chercher(id);
        mapper.update(profil, request);   // le clientId ne change pas
        return mapper.toResponse(repository.save(profil));
    }

    private ProfilCandidat chercher(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profil introuvable : " + id));
    }
    @Transactional(readOnly = true)
    public List<ProfilResponse> lister() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }
}