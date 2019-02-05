package com.phloc.commons.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Documentation measure to denote all methods which are only called from a
 * read-locked context. That is, the implementation of this method is executed
 * inside a read lock on the main monitor of this class, you do not have to care
 * about locking within this method.<br>
 * <br>
 * <b>ATTENTION: </b>Most importantly, make sure you do not try to acquire a
 * write lock from within this method to avoid deal locks!
 * 
 * @author Boris Gregorcic
 */
@Retention (RetentionPolicy.CLASS)
@Target ({ ElementType.METHOD })
@Documented
public @interface ReadLocked
{
  String value() default "";
}
