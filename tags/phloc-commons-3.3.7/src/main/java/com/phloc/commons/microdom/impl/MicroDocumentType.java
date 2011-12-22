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
package com.phloc.commons.microdom.impl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.compare.EqualsUtils;
import com.phloc.commons.microdom.IMicroDocumentType;
import com.phloc.commons.microdom.IMicroNode;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Default implementation of the {@link IMicroDocumentType} interface.
 * 
 * @author philip
 */
public final class MicroDocumentType extends AbstractMicroNode implements IMicroDocumentType
{
  private final String m_sQualifiedName;
  private final String m_sPublicID;
  private final String m_sSystemID;

  public MicroDocumentType (@Nonnull final IMicroDocumentType rhs)
  {
    this (rhs.getQualifiedName (), rhs.getPublicID (), rhs.getSystemID ());
  }

  public MicroDocumentType (@Nonnull final String sQualifiedName,
                            @Nullable final String sPublicID,
                            @Nullable final String sSystemID)
  {
    if (StringHelper.hasNoText (sQualifiedName))
      throw new IllegalArgumentException ("qualified name may not be empty");
    // publicID is null if an inline DTD is contained
    // systemID is also null if an inline DTD is contained

    m_sQualifiedName = sQualifiedName;
    m_sPublicID = sPublicID;
    m_sSystemID = sSystemID;
  }

  @Nonnull
  public String getNodeName ()
  {
    return "#doctype";
  }

  @Nonnull
  public String getQualifiedName ()
  {
    return m_sQualifiedName;
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

  @Nonnull
  public IMicroDocumentType getClone ()
  {
    return new MicroDocumentType (this);
  }

  public boolean isEqualContent (final IMicroNode o)
  {
    if (o == this)
      return true;
    if (!(o instanceof MicroDocumentType))
      return false;
    final MicroDocumentType rhs = (MicroDocumentType) o;
    return m_sQualifiedName.equals (rhs.m_sQualifiedName) &&
           EqualsUtils.nullSafeEquals (m_sPublicID, rhs.m_sPublicID) &&
           EqualsUtils.nullSafeEquals (m_sSystemID, rhs.m_sSystemID);
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ())
                            .append ("qualifiedName", m_sQualifiedName)
                            .append ("publicID", m_sPublicID)
                            .append ("systemID", m_sSystemID)
                            .toString ();
  }
}
