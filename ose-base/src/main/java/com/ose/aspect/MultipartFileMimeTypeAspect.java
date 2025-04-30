package com.ose.aspect;

import com.ose.annotation.MultipartFileMimeType;
import com.ose.exception.UnsupportedMediaTypeError;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * 上传文件类型限制切面。
 */
@Aspect
@Component
public class MultipartFileMimeTypeAspect extends BaseAspect {

    /**
     * 定义切入点：使用 @MultipartFileMimeType 注解的方法。
     */
    @Pointcut("@annotation(annotation)")
    public void controller(MultipartFileMimeType annotation) {
    }

    /**
     * 检查上传文件的类型。
     *
     * @param point      切入点信息
     * @param annotation 注解设置
     */
    @Before(value = "controller(annotation)", argNames = "point,annotation")
    public void doBefore(JoinPoint point, MultipartFileMimeType annotation) {

        String mimeTypePattern = annotation
            .value()
            .replaceAll("\\.", "\\.")
            .replaceAll("\\+", "\\+")
            .replaceAll("\\*", "[^/]+")
            .replaceAll("\\[", "\\[");

        Object[] arguments = point.getArgs();

        String contentType;

        for (Object argument : arguments) {

            if (argument == null
                || !(argument instanceof MultipartFile)
                || (contentType = ((MultipartFile) argument).getContentType()) == null) {
                continue;
            }

            if (!contentType.matches(mimeTypePattern)) {
                throw new UnsupportedMediaTypeError();
            }

        }

    }

}
