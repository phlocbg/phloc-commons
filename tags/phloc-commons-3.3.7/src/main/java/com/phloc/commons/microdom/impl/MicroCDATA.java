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

import com.phloc.commons.microdom.IMicroCDATA;
import com.phloc.commons.microdom.IMicroNode;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Default implementation of the {@link IMicroCDATA} interface.
 * 
 * @author philip
 */
public final class MicroCDATA extends AbstractMicroNode implements IMicroCDATA
{
  private final MicroDataAware m_aData;

  public MicroCDATA (@Nullable final CharSequence sText)
  {
    m_aData = new MicroDataAware (sText);
  }

  private MicroCDATA (@Nonnull final MicroDataAware aData)
  {
    m_aData = aData.getClone ();
  }

  @Nonnull
  public String getNodeName ()
  {
    return "#cdata-section";
  }

  @Override
  @Nonnull
  public String getNodeValue ()
  {
    return getData ();
  }

  @Nonnull
  public String getData ()
  {
    return m_aData.getData ().toString ();
  }

  public void appendData (@Nullable final CharSequence sData)
  {
    m_aData.appendData (sData);
  }

  public void prependData (@Nullable final CharSequence sData)
  {
    m_aData.prependData (sData);
  }

  public void setData (@Nullable final CharSequence sData)
  {
    m_aData.setData (sData);
  }

  @Nonnull
  public IMicroCDATA getClone ()
  {
    return new MicroCDATA (m_aData);
  }

  public boolean isEqualContent (final IMicroNode o)
  {
    if (o == this)
      return true;
    if (!(o instanceof MicroCDATA))
      return false;
    final MicroCDATA rhs = (MicroCDATA) o;
    return m_aData.equals (rhs.m_aData);
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ()).append ("text", getData ()).toString ();
  }
}
