package com.bin.bin_fresh_recruit_backend.interceptor;

import java.lang.annotation.*;

/**
 * 忽略Token验证
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IgnoreAuth {
    boolean required() default true;
}