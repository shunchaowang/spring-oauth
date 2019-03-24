package edu.osumc.bmi.oauth2.service.aspect;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HasRole {

  String value() default "";

  boolean owner() default false;
}
