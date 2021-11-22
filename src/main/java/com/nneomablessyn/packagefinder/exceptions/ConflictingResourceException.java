package com.nneomablessyn.packagefinder.exceptions;

public class ConflictingResourceException extends RuntimeException {

    private static final long serialVersionUID = 7312680199541896963L;

    public ConflictingResourceException(String message) {
        super(message);
    }
}
