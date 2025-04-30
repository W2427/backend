package com.ose.exception;

import org.springframework.http.HttpStatus;

public class BusinessError extends BaseError {

    private static final long serialVersionUID = 3743279294743560666L;

    public BusinessError() {
        this("error.business");
    }

    public BusinessError(String errorCode, String errorMessage) {
        this(errorCode);
        setMessage(errorMessage);
    }

    public BusinessError(String errorCode) {
        super(errorCode, HttpStatus.BAD_REQUEST);
    }

    public BusinessError(String errorCode, String[] parameters) {
        super(errorCode, HttpStatus.BAD_REQUEST, parameters);
    }

}
