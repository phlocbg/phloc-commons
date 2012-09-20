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
package com.phloc.commons.collections.attrs;

import java.util.Arrays;

import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.CGlobal;
import com.phloc.commons.lang.GenericReflection;
import com.phloc.commons.string.StringParser;

/**
 * Abstract base class for all kind of string-object mapping container. This
 * implementation provides a default implementation for all things that can be
 * independently implemented from the underlying data structure.
 * 
 * @author philip
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
  public final String getAttributeAsString (@Nullable final String sName)
  {
    return getAttributeAsString (sName, null);
  }

  @Nullable
  public static final String getAsString (@Nullable final String sParamName,
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

  public final int getAttributeAsInt (@Nullable final String sName)
  {
    return getAttributeAsInt (sName, CGlobal.ILLEGAL_UINT);
  }

  public final int getAttributeAsInt (@Nullable final String sName, final int nDefault)
  {
    final String sValue = getAttributeAsString (sName);
    return StringParser.parseInt (sValue, nDefault);
  }

  public final double getAttributeAsDouble (@Nullable final String sName)
  {
    return getAttributeAsDouble (sName, CGlobal.ILLEGAL_UINT);
  }

  public final double getAttributeAsDouble (@Nullable final String sName, final double dDefault)
  {
    final String sValue = getAttributeAsString (sName);
    return StringParser.parseDouble (sValue, dDefault);
  }

  public final boolean getAttributeAsBoolean (@Nullable final String sName)
  {
    return getAttributeAsBoolean (sName, false);
  }

  public final boolean getAttributeAsBoolean (@Nullable final String sName, final boolean bDefault)
  {
    final String sValue = getAttributeAsString (sName);
    return StringParser.parseBool (sValue, bDefault);
  }
}
