package com.ose.aspect;

import com.ose.annotation.MultipartFileMaxSize;
import com.ose.exception.PayloadTooLargeError;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * 上传文件大小限制切面。
 */
@Aspect
@Component
public class MultipartFileMaxSizeAspect extends BaseAspect {

    /**
     * 定义切入点：使用 @MultipartFileMaxSize 注解的方法。
     */
    @Pointcut("@annotation(annotation)")
    public void controller(MultipartFileMaxSize annotation) {
    }

    /**
     * 检查上传文件大小。
     *
     * @param point      切入点信息
     * @param annotation 注解设置
     */
    @Before(value = "controller(annotation)", argNames = "point,annotation")
    public void doBefore(JoinPoint point, MultipartFileMaxSize annotation) {

        long maxFileSize = annotation.value();
        Object[] arguments = point.getArgs();

        for (Object argument : arguments) {
            if (argument != null
                && argument instanceof MultipartFile
                && ((MultipartFile) argument).getSize() > maxFileSize) {
                throw new PayloadTooLargeError();
            }
        }

    }

}
