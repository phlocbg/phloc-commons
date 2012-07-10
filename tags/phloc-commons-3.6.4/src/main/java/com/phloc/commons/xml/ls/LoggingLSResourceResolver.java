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
package com.phloc.commons.xml.ls;

import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

/**
 * A logging only {@link LSResourceResolver} implementation.
 * 
 * @author philip
 */
public final class LoggingLSResourceResolver implements LSResourceResolver
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (LoggingLSResourceResolver.class);
  private final LSResourceResolver m_aParentResolver;

  public LoggingLSResourceResolver ()
  {
    this (null);
  }

  public LoggingLSResourceResolver (@Nullable final LSResourceResolver aParentResolver)
  {
    m_aParentResolver = aParentResolver;
  }

  public LSInput resolveResource (final String sType,
                                  final String sNamespaceURI,
                                  final String sPublicId,
                                  final String sSystemId,
                                  final String sBaseURI)
  {
    if (s_aLogger.isInfoEnabled ())
      s_aLogger.info ("resolveResource (" +
                      sType +
                      ", " +
                      sNamespaceURI +
                      ", " +
                      sPublicId +
                      ", " +
                      sSystemId +
                      ", " +
                      sBaseURI +
                      ")");

    // Pass to parent (if available)
    return m_aParentResolver == null ? null : m_aParentResolver.resolveResource (sType,
                                                                                 sNamespaceURI,
                                                                                 sPublicId,
                                                                                 sSystemId,
                                                                                 sBaseURI);
  }
}
