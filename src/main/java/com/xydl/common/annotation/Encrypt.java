package com.xydl.common.annotation;

import java.lang.annotation.*;

/**
 * 自定义注解加密参数
 * 
 * @author chenchen
 *
 */
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Encrypt
{

}
