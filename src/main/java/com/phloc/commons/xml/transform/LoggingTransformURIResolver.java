/**
 * Copyright (C) 2006-2011 phloc systems
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
package com.phloc.commons.xml.transform;

import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.string.ToStringGenerator;

/**
 * java.xml.transform {@link URIResolver} that logs to the commandline.
 * 
 * @author philip
 */
@NotThreadSafe
public final class LoggingTransformURIResolver implements URIResolver
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (LoggingTransformURIResolver.class);
  private final URIResolver m_aWrappedURIResolver;

  public LoggingTransformURIResolver ()
  {
    this (null);
  }

  public LoggingTransformURIResolver (@Nullable final URIResolver aWrappedURIResolver)
  {
    m_aWrappedURIResolver = aWrappedURIResolver;
  }

  public Source resolve (final String sHref, final String sBase) throws TransformerException
  {
    s_aLogger.info ("URIResolver.resolve (" + sHref + ", " + sBase + ")");
    return m_aWrappedURIResolver == null ? null : m_aWrappedURIResolver.resolve (sHref, sBase);
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).appendIfNotNull ("wrappedURIResolver", m_aWrappedURIResolver).toString ();
  }
}
