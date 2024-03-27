package ru.cwcode.cwutils.scheduler.annotationRepeatable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Repeat {
  long delay() default 1;
  
  boolean async() default false;
}
