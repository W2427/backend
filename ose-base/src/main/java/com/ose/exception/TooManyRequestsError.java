package com.ose.exception;

import org.springframework.http.HttpStatus;

public class TooManyRequestsError extends BaseError {

    private static final long serialVersionUID = -7451111322586134686L;

    public TooManyRequestsError() {
        this("error.too-many-requests");
    }

    public TooManyRequestsError(String errorCode, String errorMessage) {
        this(errorCode);
        setMessage(errorMessage);
    }

    public TooManyRequestsError(String errorCode) {
        super(errorCode, HttpStatus.TOO_MANY_REQUESTS);
    }

}
