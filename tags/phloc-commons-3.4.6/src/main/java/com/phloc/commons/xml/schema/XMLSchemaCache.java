/**
 * Copyright (C) 2006-2012 phloc systems
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
package com.phloc.commons.xml.schema;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.XMLConstants;
import javax.xml.validation.SchemaFactory;

import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.ErrorHandler;

import com.phloc.commons.xml.ls.SimpleLSResourceResolver;
import com.phloc.commons.xml.sax.LoggingSAXErrorHandler;

/**
 * This class is used to cache XML schema objects.
 * 
 * @author philip
 */
public final class XMLSchemaCache extends AbstractSchemaCache
{
  private static final XMLSchemaCache s_aInstance = new XMLSchemaCache (LoggingSAXErrorHandler.getInstance (),
                                                                        new SimpleLSResourceResolver ());
  private final SchemaFactory m_aSchemaFactory;

  public XMLSchemaCache (@Nullable final ErrorHandler aErrorHandler)
  {
    this (aErrorHandler, null);
  }

  public XMLSchemaCache (@Nullable final LSResourceResolver aResourceResolver)
  {
    this (null, aResourceResolver);
  }

  public XMLSchemaCache (@Nullable final ErrorHandler aErrorHandler,
                         @Nullable final LSResourceResolver aResourceResolver)
  {
    super ("XSD");
    m_aSchemaFactory = SchemaFactory.newInstance (XMLConstants.W3C_XML_SCHEMA_NS_URI);
    m_aSchemaFactory.setErrorHandler (aErrorHandler);
    m_aSchemaFactory.setResourceResolver (aResourceResolver);
  }

  @Nonnull
  public static XMLSchemaCache getInstance ()
  {
    return s_aInstance;
  }

  @Override
  @Nonnull
  protected SchemaFactory getSchemaFactory ()
  {
    return m_aSchemaFactory;
  }
}
