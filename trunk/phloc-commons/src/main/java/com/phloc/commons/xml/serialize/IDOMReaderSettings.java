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
package com.phloc.commons.xml.serialize;

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.validation.Schema;

import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.callback.IExceptionHandler;
import com.phloc.commons.xml.EXMLParserProperty;

/**
 * Read-only interface for DOM reader settings.
 * 
 * @author Philip Helger
 */
public interface IDOMReaderSettings
{
  /**
   * @return <code>true</code> if the parser should be namespace aware,
   *         <code>false</code> if not.
   */
  boolean isNamespaceAware ();

  /**
   * @return <code>true</code> if the parser should be validating,
   *         <code>false</code> if not.
   */
  boolean isValidating ();

  /**
   * @return <code>true</code> if the parser should be element content
   *         whitespace ignoring, <code>false</code> if not.
   */
  boolean isIgnoringElementContentWhitespace ();

  /**
   * @return <code>true</code> if the parser should expand entity references,
   *         <code>false</code> if not.
   */
  boolean isExpandEntityReferences ();

  /**
   * @return <code>true</code> if the parser should ignore comments,
   *         <code>false</code> if not.
   */
  boolean isIgnoringComments ();

  /**
   * @return <code>true</code> if the parser should read CDATA as text,
   *         <code>false</code> if not.
   */
  boolean isCoalescing ();

  /**
   * @return A special XML schema to be used or <code>null</code> if none should
   *         be used.
   */
  @Nullable
  Schema getSchema ();

  /**
   * @return <code>true</code> if the parser should be XInclude aware,
   *         <code>false</code> if not.
   */
  boolean isXIncludeAware ();

  boolean hasAnyProperties ();

  @Nullable
  Object getPropertyValue (@Nullable EXMLParserProperty eProperty);

  @Nonnull
  @ReturnsMutableCopy
  Map <EXMLParserProperty, Object> getAllPropertyValues ();

  /**
   * Check if the current settings require a separate
   * {@link DocumentBuilderFactory} or if a pooled default object can be used.
   * 
   * @return <code>true</code> if a separate {@link DocumentBuilderFactory} is
   *         required, <code>false</code> if not.
   */
  boolean requiresSeparateDocumentBuilderFactory ();

  /**
   * @return A special entity resolver to be used or <code>null</code> if no
   *         special resolver is needed.
   */
  @Nullable
  EntityResolver getEntityResolver ();

  /**
   * @return A special error handler to be used or <code>null</code> if no
   *         special error handler is needed.
   */
  @Nullable
  ErrorHandler getErrorHandler ();

  /**
   * @return A special exception handler to be used. Never <code>null</code>.
   */
  @Nonnull
  IExceptionHandler <Throwable> getExceptionHandler ();
}
