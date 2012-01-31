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
package com.phloc.commons.jmx;

import java.util.Hashtable;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import javax.management.JMException;
import javax.management.ObjectName;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.exceptions.LoggedRuntimeException;
import com.phloc.commons.lang.CGStringHelper;

@Immutable
public final class ObjectNameUtils
{
  @SuppressWarnings ("unused")
  @PresentForCodeCoverage
  private static final ObjectNameUtils s_aInstance = new ObjectNameUtils ();

  private ObjectNameUtils ()
  {}

  @Nonnull
  public static ObjectName create (@Nonnull @Nonempty final Hashtable <String, String> aParams)
  {
    if (ContainerHelper.isEmpty (aParams))
      throw new IllegalArgumentException ("JMX objectName parameters may not be empty!");
    try
    {
      return new ObjectName (CJMX.PHLOC_JMX_DOMAIN, aParams);
    }
    catch (final JMException ex)
    {
      throw LoggedRuntimeException.newException ("Failed to create ObjectName with parameter " + aParams, ex);
    }
  }

  @Nonnull
  public static ObjectName create (@Nonnull @Nonempty final Map <String, String> aParams)
  {
    if (ContainerHelper.isEmpty (aParams))
      throw new IllegalArgumentException ("JMX objectName parameters may not be empty!");
    return create (new Hashtable <String, String> (aParams));
  }

  /**
   * Create a clean property value applicable for an {@link ObjectName} property
   * value by replacing the special chars ":" and "," with "." and "//" with
   * "__". If the input value contains a blank, the quotes value is returned.
   * 
   * @param sPropertyValue
   *        The original property value. May not be <code>null</code>.
   * @return The modified property value applicable for {@link ObjectName}.
   * @see ObjectName#quote(String)
   */
  @Nonnull
  public static String getCleanPropertyValue (@Nonnull final String sPropertyValue)
  {
    // If a blank is contained, simply quote it
    if (sPropertyValue.indexOf (' ') != -1)
      return ObjectName.quote (sPropertyValue);

    // ":" is prohibited
    // "," is the property separator
    // "//" is according to the specs reserved for future use
    return sPropertyValue.replace (':', '.').replace (',', '.').replace ("//", "__");
  }

  /**
   * Create a standard {@link ObjectName} using the default domain and only the
   * "type" property. The type property is the class local name of the specified
   * object.
   * 
   * @param aObj
   *        The object from which the name is to be created.
   * @return The non-<code>null</code> {@link ObjectName}.
   */
  @Nonnull
  public static ObjectName createWithDefaultProperties (@Nonnull final Object aObj)
  {
    if (aObj == null)
      throw new NullPointerException ("object");

    final Hashtable <String, String> aParams = new Hashtable <String, String> ();
    aParams.put (CJMX.PROPERTY_TYPE, CGStringHelper.getClassLocalName (aObj));
    return create (aParams);
  }

  /**
   * Create a standard {@link ObjectName} using the default domain and the
   * "type" and "name" properties. The type property is the class local name of
   * the specified object.
   * 
   * @param aObj
   *        The object from which the name is to be created.
   * @param sName
   *        The value of the "name" JMX property
   * @return The non-<code>null</code> {@link ObjectName}.
   */
  @Nonnull
  public static ObjectName createWithDefaultProperties (@Nonnull final Object aObj, @Nonnull final String sName)
  {
    if (aObj == null)
      throw new NullPointerException ("object");
    if (sName == null)
      throw new NullPointerException ("name");

    final Hashtable <String, String> aParams = new Hashtable <String, String> ();
    aParams.put (CJMX.PROPERTY_TYPE, CGStringHelper.getClassLocalName (aObj));
    aParams.put (CJMX.PROPERTY_NAME, getCleanPropertyValue (sName));
    return create (aParams);
  }
}
