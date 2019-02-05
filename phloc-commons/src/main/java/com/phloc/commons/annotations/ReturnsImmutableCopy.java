package com.phloc.commons.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that a method returns an immutable object but containing a copy of
 * the internal state. The difference to {@link ReturnsMutableCopy} is, that the
 * returned object her is not modifiable
 * 
 * @author Boris Gregorcic
 */
@Retention (RetentionPolicy.CLASS)
@Target ({ java.lang.annotation.ElementType.METHOD })
@Documented
public @interface ReturnsImmutableCopy
{
  // empty
}
