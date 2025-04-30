package com.ose.exception;

import org.springframework.http.HttpStatus;

public class ConflictError extends BaseError {

    private static final long serialVersionUID = 9190936007714279074L;

    public ConflictError() {
        this("error.conflict");
    }

    public ConflictError(String errorCode) {
        super(errorCode, HttpStatus.CONFLICT);
    }

    public ConflictError(String errorCode, String... parameters) {
        super(errorCode, HttpStatus.CONFLICT, parameters);
    }

}
