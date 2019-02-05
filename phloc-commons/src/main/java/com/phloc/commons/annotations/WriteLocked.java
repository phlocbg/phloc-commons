package com.phloc.commons.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Documentation measure to denote all methods which are only called from a
 * write-locked context. That is, the implementation of this method is executed
 * inside a write lock on the main monitor of this class, you do not have to
 * care about locking within this method.
 * 
 * @author Boris Gregorcic
 */
@Retention (RetentionPolicy.CLASS)
@Target ({ ElementType.METHOD })
@Documented
public @interface WriteLocked
{
  String value() default "";
}
