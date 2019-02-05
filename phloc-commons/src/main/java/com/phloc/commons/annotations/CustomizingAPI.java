package com.phloc.commons.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Documentation measure to denote all classes or methods which are public JAVA
 * APIs (PublicAPI) explicitly provided for customizers. The CustomizingAPI
 * annotation should be used for all basic public services and important classes
 * a customizer needs to access. Like public APIs, CustomizingAPIs must not be
 * changed in terms of removed or renamed methods or changed method signature.
 * Only new methods may be added. Methods that should be removed are to be
 * marked as deprecated (using the <code>{@literal @}deprecated</code>
 * annotation) and can be removed in a later release.
 * 
 * @author Boris Gregorcic
 */
@Retention (RetentionPolicy.RUNTIME)
@Target ({ ElementType.TYPE,
           ElementType.FIELD,
           ElementType.METHOD,
           ElementType.PARAMETER,
           ElementType.CONSTRUCTOR,
           ElementType.LOCAL_VARIABLE,
           ElementType.ANNOTATION_TYPE,
           ElementType.PACKAGE })
@Documented
public @interface CustomizingAPI
{
  String value() default "";
}
