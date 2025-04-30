package com.ose.feign;

import com.ose.exception.BusinessError;
import com.ose.exception.UnauthorizedError;
import com.ose.response.JsonResponseBody;
import com.ose.util.StringUtils;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Feign HTTP 响应错误解码。
 */
@Component
public class ResponseErrorDecoder implements ErrorDecoder {

    /**
     * 解码 HTTP 响应中的错误。
     *
     * @param methodKey 请求方法
     * @param response  HTTP 响应
     * @return 错误对象
     */
    @Override
    public Exception decode(String methodKey, Response response) {

        try {

            if (response.body() == null) {
                if (response.status() == HttpStatus.UNAUTHORIZED.value()) {
                    throw new UnauthorizedError();
                }
                // TODO
                throw new BusinessError("error.http." + response.status());
            }

            throw StringUtils.fromJSON(
                response.body().asInputStream(),
                JsonResponseBody.class
            ).getError();

        } catch (IOException e) {
            return (new ErrorDecoder.Default()).decode(methodKey, response);
        }

    }

}
