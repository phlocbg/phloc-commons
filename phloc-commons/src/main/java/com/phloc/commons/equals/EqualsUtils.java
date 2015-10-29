/**
 * Copyright (C) 2006-2015 phloc systems
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
package com.phloc.commons.equals;

import java.util.Collection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.state.ETriState;
import com.phloc.commons.state.ITriState;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * A small helper class that provides helper methods for easy
 * <code>equals</code> method generation
 * 
 * @author Philip Helger
 */
@Immutable
public final class EqualsUtils
{
  @PresentForCodeCoverage
  private static final EqualsUtils s_aInstance = new EqualsUtils ();

  private EqualsUtils ()
  {}

  public static boolean equals (final boolean aObj1, final boolean aObj2)
  {
    return aObj1 == aObj2;
  }

  public static boolean equals (final byte aObj1, final byte aObj2)
  {
    return aObj1 == aObj2;
  }

  public static boolean equals (final char aObj1, final char aObj2)
  {
    return aObj1 == aObj2;
  }

  /**
   * Check if two double values are equal. This is necessary, because in some
   * cases, the "==" operator returns wrong results.
   * 
   * @param aObj1
   *        First double
   * @param aObj2
   *        Second double
   * @return <code>true</code> if they are equal.
   */
  public static boolean equals (final double aObj1, final double aObj2)
  {
    // ESCA-JAVA0078:
    // Special overload for "double" required!
    return (aObj1 == aObj2) || (Double.doubleToLongBits (aObj1) == Double.doubleToLongBits (aObj2));
  }

  /**
   * Check if two float values are equal. This is necessary, because in some
   * cases, the "==" operator returns wrong results.
   * 
   * @param aObj1
   *        First float
   * @param aObj2
   *        Second float
   * @return <code>true</code> if they are equal.
   */
  public static boolean equals (final float aObj1, final float aObj2)
  {
    // ESCA-JAVA0078:
    // Special overload for "float" required!
    return (aObj1 == aObj2) || (Float.floatToIntBits (aObj1) == Float.floatToIntBits (aObj2));
  }

  public static boolean equals (final int aObj1, final int aObj2)
  {
    return aObj1 == aObj2;
  }

  public static boolean equals (final long aObj1, final long aObj2)
  {
    return aObj1 == aObj2;
  }

  public static boolean equals (final short aObj1, final short aObj2)
  {
    return aObj1 == aObj2;
  }

  public static boolean equals (@Nullable final Object aObj1, @Nullable final Object aObj2)
  {
    return EqualsImplementationRegistry.areEqual (aObj1, aObj2);
  }

  @SuppressFBWarnings ({ "ES_COMPARING_PARAMETER_STRING_WITH_EQ" })
  public static boolean nullSafeEqualsIgnoreCase (@Nullable final String sObj1, @Nullable final String sObj2)
  {
    return sObj1 == null ? sObj2 == null : sObj1.equalsIgnoreCase (sObj2);
  }

  /**
   * Performs the trivial equals checks returning <code>true</code> for
   * identical objects and <code>false</code> for objects not having the same
   * implementation class. All other cases will result in <code>undefined</code>
   * and must be further compared in custom implementations
   * 
   * @param aThis
   *        must not be <code>null</code>!
   * @param aOther
   *        may be <code>null</code>
   * @return a ITriState representing the trivial equality, never
   *         <code>null</code>!
   */
  @Nonnull
  public static ITriState equalsTrivial (@Nonnull final Object aThis, @Nullable final Object aOther)
  {
    if (aThis == null)
    {
      throw new NullPointerException ("aThis"); //$NON-NLS-1$
    }
    if (aOther == aThis)
    {
      return ETriState.TRUE;
    }
    if (aOther == null || !aThis.getClass ().equals (aOther.getClass ()))
    {
      return ETriState.FALSE;
    }
    return ETriState.UNDEFINED;
  }

  /**
   * Tells whether two passed collections are equal in terms of having the same
   * content. This implementation treats a null collection same as an empty
   * collection!
   * 
   * @param aColl1
   * @param aColl2
   * @return Whether or not the collections have equal content
   */
  public static boolean equals (@Nullable final Collection <?> aColl1, @Nullable final Collection <?> aColl2)
  {
    final boolean bEmpty1 = ContainerHelper.isEmpty (aColl1);
    final boolean bEmpty2 = ContainerHelper.isEmpty (aColl2);
    if (bEmpty1 != bEmpty2)
    {
      return false;
    }
    if (!bEmpty1)
    {
      return ContainerHelper.getDifference (aColl1, aColl2).isEmpty () &&
             ContainerHelper.getDifference (aColl2, aColl1).isEmpty ();
    }
    return true;
  }
}
