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
package com.phloc.commons.collections.attrs;

import java.util.Arrays;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.CGlobal;
import com.phloc.commons.lang.GenericReflection;
import com.phloc.commons.string.StringParser;
import com.phloc.commons.typeconvert.TypeConverter;

/**
 * Abstract base class for all kind of string-object mapping container. This
 * implementation provides a default implementation for all things that can be
 * independently implemented from the underlying data structure.
 *
 * @author Philip Helger
 */
@NotThreadSafe
public abstract class AbstractReadonlyAttributeContainer implements IReadonlyAttributeContainer
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (AbstractReadonlyAttributeContainer.class);

  @Nullable
  public final <DATATYPE> DATATYPE getCastedAttribute (@Nullable final String sName)
  {
    return GenericReflection.<Object, DATATYPE> uncheckedCast (getAttributeObject (sName));
  }

  @Nullable
  public final <DATATYPE> DATATYPE getCastedAttribute (@Nullable final String sName, @Nullable final DATATYPE aDefault)
  {
    final DATATYPE aValue = this.<DATATYPE> getCastedAttribute (sName);
    return aValue == null ? aDefault : aValue;
  }

  @Nullable
  public final <DATATYPE> DATATYPE getTypedAttribute (@Nullable final String sName,
                                                      @Nonnull final Class <DATATYPE> aDstClass)
  {
    return TypeConverter.convertIfNecessary (getAttributeObject (sName), aDstClass);
  }

  @Nullable
  public final <DATATYPE> DATATYPE getTypedAttribute (@Nullable final String sName,
                                                      @Nonnull final Class <DATATYPE> aDstClass,
                                                      @Nullable final DATATYPE aDefault)
  {
    final DATATYPE aValue = this.<DATATYPE> getTypedAttribute (sName, aDstClass);
    return aValue == null ? aDefault : aValue;
  }

  @Nullable
  public final String getAttributeAsString (@Nullable final String sName)
  {
    return getAttributeAsString (sName, null);
  }

  /**
   * Get the string representation of the passed value, suitable for parameters.
   *
   * @param sParamName
   *        The name of the parameters. Has just informal character, for
   *        warnings. May be <code>null</code>.
   * @param aValue
   *        The value to be converted to a String. May be <code>null</code>.
   *        Explicitly supported data types are: String, String[]. All other
   *        data types are returned as "toString()".
   * @param sDefault
   *        The default value to be returned, if the passed value is
   *        <code>null</code> or an empty String array.
   * @return The default value if the value is <code>null</code> or an empty
   *         String array, the value as string otherwise. If the value is a
   *         String[] than the first element is returned, and the other elements
   *         are discarded.
   */
  @Nullable
  public static String getAsString (@Nullable final String sParamName,
                                    @Nullable final Object aValue,
                                    @Nullable final String sDefault)
  {
    if (aValue == null)
      return sDefault;
    if (aValue instanceof String)
      return (String) aValue;
    if (aValue instanceof String [])
    {
      // expected a single string but got an array
      final String [] aArray = (String []) aValue;
      s_aLogger.warn ("The parameter '" +
                      sParamName +
                      "' is an array with " +
                      aArray.length +
                      " items; using the first one if possible: " +
                      Arrays.toString (aArray));
      return aArray.length > 0 ? aArray[0] : sDefault;
    }

    return aValue.toString ();
  }

  @Nullable
  public final String getAttributeAsString (@Nullable final String sName, @Nullable final String sDefault)
  {
    final Object aValue = getAttributeObject (sName);
    return getAsString (sName, aValue, sDefault);
  }

  public static int getAsInt (@Nullable final String sParamName, @Nullable final Object aValue, final int nDefault)
  {
    if (aValue == null)
      return nDefault;
    if (aValue instanceof Number)
      return ((Number) aValue).intValue ();
    // Interpret as String
    final String sValue = getAsString (sParamName, aValue, null);
    return StringParser.parseInt (sValue, nDefault);
  }

  public final int getAttributeAsInt (@Nullable final String sName)
  {
    return getAttributeAsInt (sName, CGlobal.ILLEGAL_UINT);
  }

  public final int getAttributeAsInt (@Nullable final String sName, final int nDefault)
  {
    final Object aValue = getAttributeObject (sName);
    return getAsInt (sName, aValue, nDefault);
  }

  public static long getAsLong (@Nullable final String sParamName,
                                      @Nullable final Object aValue,
                                      final long nDefault)
  {
    if (aValue == null)
      return nDefault;
    if (aValue instanceof Number)
      return ((Number) aValue).longValue ();
    // Interpret as String
    final String sValue = getAsString (sParamName, aValue, null);
    return StringParser.parseLong (sValue, nDefault);
  }

  public final long getAttributeAsLong (@Nullable final String sName)
  {
    return getAttributeAsLong (sName, CGlobal.ILLEGAL_ULONG);
  }

  public final long getAttributeAsLong (@Nullable final String sName, final long nDefault)
  {
    final Object aValue = getAttributeObject (sName);
    return getAsLong (sName, aValue, nDefault);
  }

  public static double getAsDouble (@Nullable final String sParamName,
                                    @Nullable final Object aValue,
                                    final double dDefault)
  {
    if (aValue == null)
      return dDefault;
    if (aValue instanceof Number)
      return ((Number) aValue).doubleValue ();
    // Interpret as String
    final String sValue = getAsString (sParamName, aValue, null);
    return StringParser.parseDouble (sValue, dDefault);
  }

  public final double getAttributeAsDouble (@Nullable final String sName)
  {
    return getAttributeAsDouble (sName, CGlobal.ILLEGAL_UINT);
  }

  public final double getAttributeAsDouble (@Nullable final String sName, final double dDefault)
  {
    final Object aValue = getAttributeObject (sName);
    return getAsDouble (sName, aValue, dDefault);
  }

  public static boolean getAsBoolean (@Nullable final String sParamName,
                                      @Nullable final Object aValue,
                                      final boolean bDefault)
  {
    if (aValue == null)
      return bDefault;
    if (aValue instanceof Boolean)
      return ((Boolean) aValue).booleanValue ();
    // Interpret as String
    final String sValue = getAsString (sParamName, aValue, null);
    return StringParser.parseBool (sValue, bDefault);
  }

  public final boolean getAttributeAsBoolean (@Nullable final String sName)
  {
    return getAttributeAsBoolean (sName, false);
  }

  public final boolean getAttributeAsBoolean (@Nullable final String sName, final boolean bDefault)
  {
    final Object aValue = getAttributeObject (sName);
    return getAsBoolean (sName, aValue, bDefault);
  }
}
