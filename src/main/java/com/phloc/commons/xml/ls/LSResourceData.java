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
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.compare.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;

/**
 * This class encapsulates all data provided for resource resolving.
 * 
 * @author philip
 */
@Immutable
public final class LSResourceData
{
  private final String m_sType;
  private final String m_sNamespaceURI;
  private final String m_sPublicID;
  private final String m_sSystemID;
  private final String m_sBaseURI;

  public LSResourceData (@Nullable final String sType,
                         @Nullable final String sNamespaceURI,
                         @Nullable final String sPublicId,
                         @Nullable final String sSystemId,
                         @Nullable final String sBaseURI)
  {
    m_sType = sType;
    m_sNamespaceURI = sNamespaceURI;
    m_sPublicID = sPublicId;
    m_sSystemID = sSystemId;
    m_sBaseURI = sBaseURI;
  }

  @Nullable
  public String getType ()
  {
    return m_sType;
  }

  @Nullable
  public String getNamespaceURI ()
  {
    return m_sNamespaceURI;
  }

  @Nullable
  public String getPublicID ()
  {
    return m_sPublicID;
  }

  @Nullable
  public String getSystemID ()
  {
    return m_sSystemID;
  }

  @Nullable
  public String getBaseURI ()
  {
    return m_sBaseURI;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof LSResourceData))
      return false;
    final LSResourceData rhs = (LSResourceData) o;
    return EqualsUtils.nullSafeEquals (m_sType, rhs.m_sType) &&
           EqualsUtils.nullSafeEquals (m_sNamespaceURI, rhs.m_sNamespaceURI) &&
           EqualsUtils.nullSafeEquals (m_sPublicID, rhs.m_sPublicID) &&
           EqualsUtils.nullSafeEquals (m_sSystemID, rhs.m_sSystemID) &&
           EqualsUtils.nullSafeEquals (m_sBaseURI, rhs.m_sBaseURI);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sType)
                                       .append (m_sNamespaceURI)
                                       .append (m_sPublicID)
                                       .append (m_sSystemID)
                                       .append (m_sBaseURI)
                                       .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("type", m_sType)
                                       .append ("namespaceURI", m_sNamespaceURI)
                                       .append ("publicId", m_sPublicID)
                                       .append ("systemId", m_sSystemID)
                                       .append ("baseURI", m_sBaseURI)
                                       .toString ();
  }
}
