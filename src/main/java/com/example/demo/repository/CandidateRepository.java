package com.example.demo.repository;

import com.example.demo.domain.Candidate;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateRepository extends PagingAndSortingRepository<Candidate, Long> {

}
