package com.ose.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.ose.constant.FileSizeValues.MB;

/**
 * REST 控制器上传文件大小限制。
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MultipartFileMaxSize {

    // 文件大小上线（字节）
    long value() default MB;

}
