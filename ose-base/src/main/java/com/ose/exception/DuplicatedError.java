package com.ose.exception;

public class DuplicatedError extends ConflictError {

    private static final long serialVersionUID = -1182594666355297225L;

    public DuplicatedError() {
        this("error.duplicated");
    }

    public DuplicatedError(String code) {
        super(code);
    }

    public DuplicatedError(String code, String... parameters) {
        super(code, parameters);
    }

}
