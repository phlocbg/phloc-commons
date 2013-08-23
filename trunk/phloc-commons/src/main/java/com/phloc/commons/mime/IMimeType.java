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
import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.IHasStringRepresentation;
import com.phloc.commons.annotations.MustImplementEqualsAndHashcode;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsMutableCopy;

/**
 * Interface for the structured representation of a single MIME type.
 * 
 * @author Philip Helger
 */
@MustImplementEqualsAndHashcode
public interface IMimeType extends IHasStringRepresentation, Serializable
{
  /** The default quoting algorithm to be used */
  EMimeQuoting DEFAULT_QUOTING = EMimeQuoting.QUOTED_STRING;

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
   * Get the MIME type including all parameters as a single string. By default
   * the {@link #DEFAULT_QUOTING} quoting algorithm is used.
   * 
   * @return The combined string to be used as text representation:
   *         <code><em>contentType</em> '/' <em>subType</em> ( ';' <em>parameterName</em> '=' <em>parameterValue</em> )*</code>
   * @see #getAsString(EMimeQuoting)
   * @see #getAsStringWithoutParameters()
   */
  @Nonnull
  String getAsString ();

  /**
   * Get the MIME type including all parameters as a single string. The
   * specified quoting algorithm is used to quote parameter values (if
   * necessary).
   * 
   * @return The combined string to be used as text representation:
   *         <code><em>contentType</em> '/' <em>subType</em> ( ';' <em>parameterName</em> '=' <em>parameterValue</em> )*</code>
   * @see #getAsStringWithoutParameters()
   */
  @Nonnull
  String getAsString (@Nonnull EMimeQuoting eQuotingAlgorithm);

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

  /**
   * @return <code>true</code> if at least one parameter is present,
   *         <code>false</code> if no parameter is present.
   */
  boolean hasAnyParameters ();

  /**
   * @return The number of parameters. Alway &ge; 0.
   */
  @Nonnegative
  int getParameterCount ();

  /**
   * @return All present parameters. May not be <code>null</code> but empty.
   */
  @Nonnull
  @ReturnsMutableCopy
  List <MimeTypeParameter> getAllParameters ();

  /**
   * Get the parameter at the specified index.
   * 
   * @param nIndex
   *        The index to use. Should be &ge; 0.
   * @return <code>null</code> if the provided index is illegal.
   */
  @Nullable
  MimeTypeParameter getParameterAtIndex (@Nonnegative int nIndex);
}
