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
package com.phloc.commons.mime;

import java.io.Serializable;

import javax.annotation.Nonnull;

import com.phloc.commons.IHasStringRepresentation;
import com.phloc.commons.annotations.MustImplementEqualsAndHashcode;
import com.phloc.commons.annotations.Nonempty;

/**
 * Interface for the structured representation of a single MIME type.
 * 
 * @author Philip Helger
 */
@MustImplementEqualsAndHashcode
public interface IMimeType extends IHasStringRepresentation, Serializable
{
  /**
   * @return The content type. Never <code>null</code>.
   */
  @Nonnull
  EMimeContentType getContentType ();

  /**
   * @return The content sub type. Never <code>null</code>.
   */
  @Nonnull
  String getContentSubType ();

  /**
   * @return The combined string to be used as text representation:
   *         <code><em>contentType</em> '/' <em>subType</em> ( ';' <em>parameterName</em> '=' <em>parameterValue</em> )*</code>
   * @see #getAsStringWithoutParameters()
   */
  @Nonnull
  String getAsString ();

  /**
   * @return The combined string to be used as text representation but without
   *         the parameters:
   *         <code><em>contentType</em> '/' <em>subType</em></code>
   * @see #getAsString()
   */
  @Nonnull
  String getAsStringWithoutParameters ();

  /**
   * @param sEncoding
   *        The encoding to use. May neither be <code>null</code> nor empty.
   * @return The combined string plus the passed encoding.
   * @see CMimeType#CHARSET_PREFIX
   * @see #getAsString()
   */
  @Nonnull
  @Deprecated
  String getAsStringWithEncoding (@Nonnull @Nonempty String sEncoding);
}
