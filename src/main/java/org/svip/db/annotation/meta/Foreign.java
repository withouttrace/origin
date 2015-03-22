package org.svip.db.annotation.meta;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Foreign{
    public String name() default "";

    public String column() default "";

    public String refTable() default "";

    public String refColumn() default "";

    public String database() default "";
}