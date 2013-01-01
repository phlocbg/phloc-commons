/**
 * Copyright (C) 2006-2013 phloc systems
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
 * Marker interface that claims that implementations of this interface are
 * loaded via the {@link com.phloc.commons.lang.ServiceLoaderBackport} (from
 * Java 1.6 on you may use java.util.ServiceLoader). This implies that the
 * package and source file name should never change. SPI interfaces should also
 * have the suffix SPI in their name (e.g. <code>IServiceSPI</code>).
 * 
 * @author philip
 */
@Retention (RetentionPolicy.CLASS)
@Target ({ ElementType.TYPE })
@Documented
public @interface IsSPIInterface
{
  String value() default "";
}
