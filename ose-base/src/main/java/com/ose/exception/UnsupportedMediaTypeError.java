package com.ose.exception;

import org.springframework.http.HttpStatus;

/**
 * 媒体类型不被支持错误。
 */
public class UnsupportedMediaTypeError extends BaseError {

    private static final long serialVersionUID = 3039684466257739351L;

    /**
     * 构造方法。
     */
    public UnsupportedMediaTypeError(String... parameters) {
        super("error.unsupported-mime-type", HttpStatus.UNSUPPORTED_MEDIA_TYPE, parameters);
    }

}
