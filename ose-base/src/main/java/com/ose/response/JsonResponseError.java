package com.ose.response;

import com.ose.exception.ValidationError;
import com.ose.exception.BaseError;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * HTTP 响应错误信息。
 */
public class JsonResponseError extends BaseError implements Serializable {

    private static final long serialVersionUID = 1596615723843538309L;

    @Schema(description = "发生校验错误的字段的列表")
    private List<FieldError> fields = null;

    @Schema(description = "错误列表")
    private List<JsonResponseError> errors = null;

    /**
     * 构造方法。
     */
    public JsonResponseError() {
    }

    /**
     * 构造方法。
     *
     * @param exception 异常实例
     */
    JsonResponseError(Exception exception) {

        super(exception);

        if (exception instanceof ValidationError) {

            ValidationError validationError = (ValidationError) exception;

            List<org.springframework.validation.FieldError> fieldErrors = validationError.getFields();

            if (fieldErrors != null && fieldErrors.size() > 0) {
                fields = new ArrayList<>();
                fieldErrors.forEach(fieldError -> fields.add(new FieldError(fieldError)));
            }

            List<ValidationError> validationErrors = validationError.getErrors();

            if (validationErrors != null && validationErrors.size() > 0) {
                errors = new ArrayList<>();
                validationErrors.forEach(item -> errors.add(new JsonResponseError(item)));
            }

        }

    }

    public List<FieldError> getFields() {
        return fields;
    }

    public List<JsonResponseError> getErrors() {
        return errors;
    }

    private static class FieldError {

        @Schema(description = "发生错误的字段")
        private String name;

        @Schema(description = "错误类型")
        private String type;

        @Schema(description = "错误描述")
        private String message;

        public FieldError() {
            super();
        }

        FieldError(org.springframework.validation.FieldError fieldError) {
            this.name = fieldError.getField();
            this.type = fieldError.getCode();
            this.message = fieldError.getDefaultMessage();
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public String getMessage() {
            return message;
        }

    }

}
