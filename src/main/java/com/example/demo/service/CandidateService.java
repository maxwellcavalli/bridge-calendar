package com.example.demo.service;

import com.example.demo.domain.Candidate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CandidateService {

    Candidate create(Candidate candidate);

    Candidate update(Long id, Candidate candidate);

    void delete(long id);

    Optional<Candidate> get(long id);

    Page<Candidate> list(final Pageable pageable);
}
