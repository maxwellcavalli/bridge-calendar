package com.example.demo.service.impl;

import com.example.demo.domain.Candidate;
import com.example.demo.exception.CandidateNotFoundException;
import com.example.demo.repository.CandidateRepository;
import com.example.demo.service.CandidateService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class DefaultCandidateService implements CandidateService {

    private final CandidateRepository candidateRepository;

    @Override
    public Candidate create(final Candidate candidate) {
        return candidateRepository.save(candidate);
    }

    @Override
    public Candidate update(final Long id, final Candidate candidate) {
        return candidateRepository.findById(id)
                .map(db -> {
                    db.setName(candidate.getName());
                    db.setCalendarAvailabilities(candidate.getCalendarAvailabilities());
                    return candidateRepository.save(db);
                }).orElseThrow(() -> new CandidateNotFoundException(String.valueOf(id)));
    }

    @Override
    public void delete(final long id) {
        candidateRepository.findById(id)
                .map(candidate -> {
                    candidateRepository.delete(candidate);
                    return true;
                })
                .orElseThrow(() -> new CandidateNotFoundException(String.valueOf(id)));
    }

    @Override
    public Optional<Candidate> get(long id) {
        return Optional.ofNullable(candidateRepository.findById(id)
                .orElseThrow(() -> new CandidateNotFoundException(String.valueOf(id))));
    }

    @Override
    public Page<Candidate> list(final Pageable pageable) {
        return candidateRepository.findAll(pageable);
    }

}
