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

import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.ext.LexicalHandler;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.callback.IExceptionHandler;
import com.phloc.commons.xml.EXMLParserFeature;

/**
 * Read only interface for the {@link SAXReaderSettings}.
 * 
 * @author Philip Helger
 */
public interface ISAXReaderSettings
{
  /**
   * @return The special entity resolver to be used. May be <code>null</code>.
   */
  @Nullable
  EntityResolver getEntityResolver ();

  /**
   * @return The special DTD handler to be used. May be <code>null</code>.
   */
  @Nullable
  DTDHandler getDTDHandler ();

  /**
   * @return The special content handler to be used. May be <code>null</code>.
   */
  @Nullable
  ContentHandler getContentHandler ();

  /**
   * @return The special error handler to be used. May be <code>null</code>.
   */
  @Nullable
  ErrorHandler getErrorHandler ();

  /**
   * @return The special lexical handler to be used. May be <code>null</code>.
   */
  @Nullable
  LexicalHandler getLexicalHandler ();

  /**
   * @return <code>true</code> if at least one parser feature is defined,
   *         <code>false</code> if not
   */
  boolean hasAnyFeature ();

  /**
   * Get the value of the specified parser feature
   * 
   * @param eFeature
   *        The feature to search. May be <code>null</code>.
   * @return <code>null</code> if this feature is undefined.
   */
  @Nullable
  Boolean getFeatureValue (@Nullable EXMLParserFeature eFeature);

  /**
   * @return A copy of all defined parser features at the associated values.
   */
  @Nonnull
  @ReturnsMutableCopy
  Map <EXMLParserFeature, Boolean> getAllFeatures ();

  /**
   * @return The exception handler to be used.
   */
  @Nonnull
  IExceptionHandler <Throwable> getExceptionHandler ();
}
