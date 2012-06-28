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
package com.phloc.commons.microdom.impl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.ICloneable;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.microdom.IMicroDataAware;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Default implementation of the {@link IMicroDataAware} interface.
 * 
 * @author philip
 */
final class MicroDataAware implements IMicroDataAware, ICloneable <MicroDataAware>
{
  private final StringBuilder m_aData;

  public MicroDataAware (@Nullable final CharSequence sText)
  {
    if (StringHelper.hasNoText (sText))
      m_aData = new StringBuilder ();
    else
      m_aData = new StringBuilder (sText);
  }

  @Nonnull
  public CharSequence getData ()
  {
    return m_aData;
  }

  public void setData (@Nullable final CharSequence aData)
  {
    m_aData.setLength (0);
    m_aData.append (aData);
  }

  public void appendData (@Nullable final CharSequence sData)
  {
    m_aData.append (sData);
  }

  public void prependData (@Nullable final CharSequence sData)
  {
    m_aData.insert (0, sData);
  }

  @Nonnull
  public MicroDataAware getClone ()
  {
    return new MicroDataAware (m_aData);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof MicroDataAware))
      return false;
    final MicroDataAware rhs = (MicroDataAware) o;
    return m_aData.toString ().equals (rhs.m_aData.toString ());
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aData.toString ()).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("data", m_aData).toString ();
  }
}
