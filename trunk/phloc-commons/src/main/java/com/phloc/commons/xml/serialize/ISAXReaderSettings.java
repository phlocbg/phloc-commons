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
  @Nullable
  EntityResolver getEntityResolver ();

  @Nullable
  DTDHandler getDTDHandler ();

  @Nullable
  ContentHandler getContentHandler ();

  @Nullable
  ErrorHandler getErrorHandler ();

  @Nullable
  LexicalHandler getLexicalHandler ();

  @Nullable
  Boolean getParserFeatureValue (@Nullable EXMLParserFeature eFeature);

  boolean hasParserFeatureValues ();

  @Nonnull
  @ReturnsMutableCopy
  Map <EXMLParserFeature, Boolean> getAllParserFeatureValues ();

  @Nonnull
  IExceptionHandler <Throwable> getExceptionHandler ();
}
