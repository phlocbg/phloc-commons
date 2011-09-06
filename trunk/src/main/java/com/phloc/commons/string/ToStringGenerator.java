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
package com.phloc.commons.string;

import java.util.Arrays;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

/**
 * This is a utility class for easier "toString" method creations.
 * 
 * @author philip
 */
@NotThreadSafe
public final class ToStringGenerator
{
  private static final int FIRST_FIELD = 1;
  private static final int APPENDED_CLOSING_BRACKET = 2;

  private final StringBuilder m_aSB = new StringBuilder ("[");
  private int m_nIndex = 0;

  public ToStringGenerator (@Nullable final Object aSrc)
  {
    if (aSrc != null)
    {
      final String sClassName = aSrc.getClass ().getName ();
      final int nIndex = sClassName.lastIndexOf ('.');
      m_aSB.append (nIndex == -1 ? sClassName : sClassName.substring (nIndex + 1))
           .append ("@0x")
           .append (StringHelper.hexStringLeadingZero (System.identityHashCode (aSrc), 8));
    }
  }

  private void _beforeAddField ()
  {
    if ((m_nIndex & FIRST_FIELD) == 0)
    {
      m_nIndex |= FIRST_FIELD;

      // Only if a valid source object was provided
      if (m_aSB.length () > 1)
        m_aSB.append (": ");
    }
    else
      m_aSB.append ("; ");
  }

  @Nonnull
  private ToStringGenerator _appendSuper (final String sSuper)
  {
    _beforeAddField ();
    m_aSB.append (sSuper);
    return this;
  }

  @Nonnull
  public ToStringGenerator append (@Nonnull final String sField, final boolean aValue)
  {
    _beforeAddField ();
    m_aSB.append (sField).append ('=').append (aValue);
    return this;
  }

  @Nonnull
  public ToStringGenerator append (@Nonnull final String sField, @Nullable final boolean [] aValue)
  {
    _beforeAddField ();
    m_aSB.append (sField).append ('=').append (Arrays.toString (aValue));
    return this;
  }

  @Nonnull
  public ToStringGenerator append (@Nonnull final String sField, final byte aValue)
  {
    _beforeAddField ();
    m_aSB.append (sField).append ('=').append (aValue);
    return this;
  }

  @Nonnull
  public ToStringGenerator append (@Nonnull final String sField, @Nullable final byte [] aValue)
  {
    _beforeAddField ();
    m_aSB.append (sField).append ('=').append (Arrays.toString (aValue));
    return this;
  }

  @Nonnull
  public ToStringGenerator append (@Nonnull final String sField, final char aValue)
  {
    _beforeAddField ();
    m_aSB.append (sField).append ('=').append (aValue);
    return this;
  }

  @Nonnull
  public ToStringGenerator append (@Nonnull final String sField, @Nullable final char [] aValue)
  {
    _beforeAddField ();
    m_aSB.append (sField).append ('=').append (Arrays.toString (aValue));
    return this;
  }

  @Nonnull
  public ToStringGenerator append (@Nonnull final String sField, final double aValue)
  {
    _beforeAddField ();
    m_aSB.append (sField).append ('=').append (aValue);
    return this;
  }

  @Nonnull
  public ToStringGenerator append (@Nonnull final String sField, @Nullable final double [] aValue)
  {
    _beforeAddField ();
    m_aSB.append (sField).append ('=').append (Arrays.toString (aValue));
    return this;
  }

  @Nonnull
  public ToStringGenerator append (@Nonnull final String sField, final float aValue)
  {
    _beforeAddField ();
    m_aSB.append (sField).append ('=').append (aValue);
    return this;
  }

  @Nonnull
  public ToStringGenerator append (@Nonnull final String sField, @Nullable final float [] aValue)
  {
    _beforeAddField ();
    m_aSB.append (sField).append ('=').append (Arrays.toString (aValue));
    return this;
  }

  @Nonnull
  public ToStringGenerator append (@Nonnull final String sField, final int aValue)
  {
    _beforeAddField ();
    m_aSB.append (sField).append ('=').append (aValue);
    return this;
  }

  @Nonnull
  public ToStringGenerator append (@Nonnull final String sField, @Nullable final int [] aValue)
  {
    _beforeAddField ();
    m_aSB.append (sField).append ('=').append (Arrays.toString (aValue));
    return this;
  }

  @Nonnull
  public ToStringGenerator append (@Nonnull final String sField, final long aValue)
  {
    _beforeAddField ();
    m_aSB.append (sField).append ('=').append (aValue);
    return this;
  }

  @Nonnull
  public ToStringGenerator append (@Nonnull final String sField, @Nullable final long [] aValue)
  {
    _beforeAddField ();
    m_aSB.append (sField).append ('=').append (Arrays.toString (aValue));
    return this;
  }

  @Nonnull
  public ToStringGenerator append (@Nonnull final String sField, final short aValue)
  {
    _beforeAddField ();
    m_aSB.append (sField).append ('=').append (aValue);
    return this;
  }

  @Nonnull
  public ToStringGenerator append (@Nonnull final String sField, @Nullable final short [] aValue)
  {
    _beforeAddField ();
    m_aSB.append (sField).append ('=').append (Arrays.toString (aValue));
    return this;
  }

  @Nonnull
  public ToStringGenerator appendPassword (@Nonnull final String sField)
  {
    return append (sField, "****");
  }

  @Nonnull
  public ToStringGenerator append (@Nonnull final String sField, @Nullable final Enum <?> aValue)
  {
    return append (sField, String.valueOf (aValue));
  }

  @Nonnull
  public ToStringGenerator append (@Nonnull final String sField, @Nullable final Object aValue)
  {
    if (aValue != null && aValue.getClass ().isArray ())
    {
      final Class <?> aCompType = aValue.getClass ().getComponentType ();
      if (aCompType.equals (boolean.class))
        return append (sField, (boolean []) aValue);
      if (aCompType.equals (byte.class))
        return append (sField, (byte []) aValue);
      if (aCompType.equals (char.class))
        return append (sField, (char []) aValue);
      if (aCompType.equals (double.class))
        return append (sField, (double []) aValue);
      if (aCompType.equals (float.class))
        return append (sField, (float []) aValue);
      if (aCompType.equals (int.class))
        return append (sField, (int []) aValue);
      if (aCompType.equals (long.class))
        return append (sField, (long []) aValue);
      if (aCompType.equals (short.class))
        return append (sField, (short []) aValue);
      return append (sField, (Object []) aValue);
    }
    _beforeAddField ();
    m_aSB.append (sField).append ('=').append (aValue);
    return this;
  }

  @Nonnull
  public ToStringGenerator append (@Nonnull final String sField, @Nullable final Object [] aValue)
  {
    _beforeAddField ();
    m_aSB.append (sField).append ('=').append (Arrays.toString (aValue));
    return this;
  }

  @Nonnull
  public ToStringGenerator appendIfNotNull (@Nonnull final String sField, @Nullable final boolean [] aValue)
  {
    return aValue == null ? this : append (sField, aValue);
  }

  @Nonnull
  public ToStringGenerator appendIfNotNull (@Nonnull final String sField, @Nullable final byte [] aValue)
  {
    return aValue == null ? this : append (sField, aValue);
  }

  @Nonnull
  public ToStringGenerator appendIfNotNull (@Nonnull final String sField, @Nullable final char [] aValue)
  {
    return aValue == null ? this : append (sField, aValue);
  }

  @Nonnull
  public ToStringGenerator appendIfNotNull (@Nonnull final String sField, @Nullable final double [] aValue)
  {
    return aValue == null ? this : append (sField, aValue);
  }

  @Nonnull
  public ToStringGenerator appendIfNotNull (@Nonnull final String sField, @Nullable final float [] aValue)
  {
    return aValue == null ? this : append (sField, aValue);
  }

  @Nonnull
  public ToStringGenerator appendIfNotNull (@Nonnull final String sField, @Nullable final int [] aValue)
  {
    return aValue == null ? this : append (sField, aValue);
  }

  @Nonnull
  public ToStringGenerator appendIfNotNull (@Nonnull final String sField, @Nullable final long [] aValue)
  {
    return aValue == null ? this : append (sField, aValue);
  }

  @Nonnull
  public ToStringGenerator appendIfNotNull (@Nonnull final String sField, @Nullable final short [] aValue)
  {
    return aValue == null ? this : append (sField, aValue);
  }

  @Nonnull
  public ToStringGenerator appendIfNotNull (@Nonnull final String sField, @Nullable final Object aValue)
  {
    return aValue == null ? this : append (sField, aValue);
  }

  @Nonnull
  public ToStringGenerator appendIfNotNull (@Nonnull final String sField, @Nullable final Object [] aValue)
  {
    return aValue == null ? this : append (sField, aValue);
  }

  @Override
  @Nonnull
  public String toString ()
  {
    if ((m_nIndex & APPENDED_CLOSING_BRACKET) == 0)
    {
      m_nIndex |= APPENDED_CLOSING_BRACKET;
      m_aSB.append (']');
    }
    return m_aSB.toString ();
  }

  /**
   * Create a {@link ToStringGenerator} for derived classes where the base class
   * also uses the {@link ToStringGenerator}. This avoids that the implementing
   * class name is emitted more than once.
   * 
   * @param sSuperToString
   *        Always pass in <code>super.toString ()</code>
   * @return Never <code>null</code>
   */
  @Nonnull
  public static ToStringGenerator getDerived (@Nonnull final String sSuperToString)
  {
    // We don't need the object is "super.toString" is involved, because in
    // super.toString the object is already emitted!
    return new ToStringGenerator (null)._appendSuper (sSuperToString);
  }
}
