package com.ose.exception;

import org.springframework.http.HttpStatus;

public class ServiceUnavailableError extends BaseError {

    private static final long serialVersionUID = 7480143797493354253L;

    public ServiceUnavailableError() {
        super("error.service-unavailable", HttpStatus.SERVICE_UNAVAILABLE);
    }

}
