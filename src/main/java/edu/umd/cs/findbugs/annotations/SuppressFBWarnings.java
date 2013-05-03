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
package edu.umd.cs.findbugs.annotations;

/*
 * FindBugs
 * Copyright (C) 2011 University of Maryland
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention (RetentionPolicy.CLASS)
/**
 * This annotation is used to suppress FindBugs warnings. It can be used instead of 
 * {@link edu.umd.cs.findbugs.annotations.SuppressWarnings} to avoid conflicts with
 * {@link java.lang.SuppressWarnings}. 
 * 
 */
public @interface SuppressFBWarnings
{
  /**
   * The set of FindBugs warnings that are to be suppressed in annotated
   * element. The value can be a bug category, kind or pattern.
   */
  String [] value() default {};

  String justification() default "";
}
