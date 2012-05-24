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

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.microdom.EMicroNodeType;
import com.phloc.commons.microdom.IMicroNode;
import com.phloc.commons.microdom.IMicroText;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Default implementation of the {@link IMicroText} interface.
 * 
 * @author philip
 */
public final class MicroText extends AbstractMicroNode implements IMicroText
{
  public static final boolean DEFAULT_IGNORABLE_WHITESPACE = false;

  private final MicroDataAware m_aData;
  private final boolean m_bIgnorableWhitespace;

  public MicroText (@Nullable final CharSequence sText)
  {
    this (sText, DEFAULT_IGNORABLE_WHITESPACE);
  }

  public MicroText (@Nullable final CharSequence sText, final boolean bIgnorableWhitespace)
  {
    m_aData = new MicroDataAware (sText);
    m_bIgnorableWhitespace = bIgnorableWhitespace;
  }

  private MicroText (@Nonnull final MicroDataAware aData, final boolean bIgnorableWhitespace)
  {
    m_aData = aData.getClone ();
    m_bIgnorableWhitespace = bIgnorableWhitespace;
  }

  @Nonnull
  public EMicroNodeType getType ()
  {
    return EMicroNodeType.TEXT;
  }

  @Nonnull
  @Nonempty
  public String getNodeName ()
  {
    return "#text";
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

  public boolean isElementContentWhitespace ()
  {
    return m_bIgnorableWhitespace;
  }

  @Nonnull
  public IMicroText getClone ()
  {
    return new MicroText (m_aData, m_bIgnorableWhitespace);
  }

  public boolean isEqualContent (final IMicroNode o)
  {
    if (o == this)
      return true;
    if (!(o instanceof MicroText))
      return false;
    final MicroText rhs = (MicroText) o;
    return m_aData.equals (rhs.m_aData) && m_bIgnorableWhitespace == rhs.m_bIgnorableWhitespace;
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ())
                            .append ("text", getData ())
                            .append ("ignorableWhitspace", m_bIgnorableWhitespace)
                            .toString ();
  }
}
