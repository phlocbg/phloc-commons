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
package com.phloc.commons.microdom.impl;

import javax.annotation.Nonnegative;
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
  /** By default the text is not ignorable whitespaces */
  public static final boolean DEFAULT_IGNORABLE_WHITESPACE = false;
  /** By default the text should be XML escaped when emitting to a file etc. */
  public static final boolean DEFAULT_ESCAPE = true;

  private final MicroDataAware m_aData;
  private final boolean m_bIgnorableWhitespace;
  private boolean m_bEscape = DEFAULT_ESCAPE;

  public MicroText (@Nonnull final char [] aChars,
                    @Nonnegative final int nOfs,
                    @Nonnegative final int nLen,
                    final boolean bIgnorableWhitespace)
  {
    m_aData = new MicroDataAware (aChars, nOfs, nLen);
    m_bIgnorableWhitespace = bIgnorableWhitespace;
  }

  public MicroText (@Nullable final CharSequence sText)
  {
    this (sText, DEFAULT_IGNORABLE_WHITESPACE);
  }

  public MicroText (@Nullable final CharSequence sText, final boolean bIgnorableWhitespace)
  {
    m_aData = new MicroDataAware (sText);
    m_bIgnorableWhitespace = bIgnorableWhitespace;
  }

  /**
   * Constructor for cloning
   * 
   * @param aData
   *        Cloned data
   * @param bIgnorableWhitespace
   *        ignorable whitespace?
   * @param bEscape
   *        escape text as XML when writing?
   */
  private MicroText (@Nonnull final MicroDataAware aData, final boolean bIgnorableWhitespace, final boolean bEscape)
  {
    m_aData = aData;
    m_bIgnorableWhitespace = bIgnorableWhitespace;
    m_bEscape = bEscape;
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
    return getData ().toString ();
  }

  @Nonnull
  public CharSequence getData ()
  {
    return m_aData.getData ();
  }

  public void appendData (@Nullable final CharSequence sData)
  {
    m_aData.appendData (sData);
  }

  public void appendData (@Nonnull final char [] aChars, @Nonnegative final int nOfs, @Nonnegative final int nLen)
  {
    m_aData.appendData (aChars, nOfs, nLen);
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

  public boolean isEscape ()
  {
    return m_bEscape;
  }

  @Nonnull
  public MicroText setEscape (final boolean bEscape)
  {
    m_bEscape = bEscape;
    return this;
  }

  @Nonnull
  public IMicroText getClone ()
  {
    return new MicroText (m_aData.getClone (), m_bIgnorableWhitespace, m_bEscape);
  }

  public boolean isEqualContent (@Nullable final IMicroNode o)
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
