package com.example.demo.exception;

public class CandidateNotFoundException extends DataNotFoundException {

    public CandidateNotFoundException(String message) {
        super("Candidate not found " + message);
    }
}
