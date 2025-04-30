package com.ose.exception;

import org.springframework.http.HttpStatus;

/**
 * 请求数据过大错误。
 */
public class PayloadTooLargeError extends BaseError {

    private static final long serialVersionUID = -1883645509061082968L;

    /**
     * 构造方法。
     * TODO 添加最大限制参数
     */
    public PayloadTooLargeError() {
        super("error.payload-too-large", HttpStatus.PAYLOAD_TOO_LARGE);
    }

}
