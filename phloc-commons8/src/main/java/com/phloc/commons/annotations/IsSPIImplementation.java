/**
 * Copyright (C) 2006-2014 phloc systems
 * http://www.phloc.com
 * office[at]phloc[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.phloc.commons.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker interface that claims that this class implements an SPI interface that
 * is resolved via the {@link java.util.ServiceLoader}. This is mainly for
 * checking that this class must be public and requires a public no-argument
 * constructor and that no references from any other Java source file may be
 * present. If this annotation is used, it implies that the semantics of
 * {@link UsedViaReflection} also apply.
 * 
 * @author Philip Helger
 */
@Retention (RetentionPolicy.CLASS)
@Target ({ ElementType.TYPE })
@Documented
// IFJDK5
// @SuppressWarnings ("javadoc")
// ELSE
// ENDIF
public @interface IsSPIImplementation
{
  String value() default "";
}
