package com.hj.springconfig;


import org.springframework.stereotype.Indexed;

import java.lang.annotation.*;

/**
 * 文件加水印注解
 *
 * 这里采用 spel 表达式 完成适配
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Indexed
public @interface AddWaterMark {
    /**
     * 是否增加 spel 解析出 bool 值
     */
    String whetherAdd() default "";


    /**
     * 增加内容 spel 解析出 String 值
     */
    String content() default "";

}
