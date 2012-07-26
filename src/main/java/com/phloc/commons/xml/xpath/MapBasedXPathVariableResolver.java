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
package com.phloc.commons.xml.xpath;

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;
import javax.xml.namespace.QName;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathVariableResolver;

/**
 * Class is used in conjunction with {@link XPathExpression} to resolve variable
 * values used in XPath queries at runtime. The local part of the QName to
 * resolve is used as the key in the map.
 * 
 * @author philip
 */
@NotThreadSafe
public final class MapBasedXPathVariableResolver implements XPathVariableResolver
{
  private final Map <String, Object> m_aVars;

  public MapBasedXPathVariableResolver (@Nullable final Map <String, Object> aVars)
  {
    m_aVars = aVars;
  }

  @Nullable
  public Object resolveVariable (@Nonnull final QName aVariableName)
  {
    if (aVariableName == null)
      throw new NullPointerException ("variableName");

    if (m_aVars == null)
      return null;

    final String sLocalName = aVariableName.getLocalPart ();
    return m_aVars.get (sLocalName);
  }
}
