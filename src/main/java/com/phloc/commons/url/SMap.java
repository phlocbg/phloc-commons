/**
 * Copyright (C) 2006-2014 phloc systems
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
package com.phloc.commons.url;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.ICloneable;
import com.phloc.commons.annotations.DevelopersNote;

/**
 * A special URL parameter map that is made for best inline usage. It's simply a
 * Map&lt;String,String&gt; with more nifty API :)<br>
 * SMap is short for String-Map
 * 
 * @author Philip Helger
 */
@NotThreadSafe
public class SMap extends LinkedHashMap <String, String> implements ICloneable <SMap>
{
  /**
   * Create an empty map.
   */
  public SMap ()
  {}

  /**
   * Create a map base on the given map. Kind of a copy-constructor.
   * 
   * @param rhs
   *        The map to add. May not be <code>null</code>.
   */
  public SMap (@Nullable final Map <String, String> rhs)
  {
    if (rhs != null)
      super.putAll (rhs);
  }

  public SMap (@Nonnull final String sName, @Nonnull final String sValue)
  {
    add (sName, sValue);
  }

  public SMap (@Nonnull final String sName, @Nonnull final int nValue)
  {
    add (sName, nValue);
  }

  public SMap (@Nonnull final String sName, @Nonnull final Locale aValue)
  {
    add (sName, aValue);
  }

  /**
   * Important: this method must be present, because the underlying AbstractMap
   * otherwise throws an exception if this method is not overridden!!!
   * 
   * @param sName
   *        Key
   * @param sValue
   *        Value
   * @return The previously assigned value for the given key or
   *         <code>null</code> if no previously assigned value is present.
   */
  @Override
  @Deprecated
  @DevelopersNote ("Use add instead - only for API compliance!")
  public String put (@Nonnull final String sName, @Nonnull final String sValue)
  {
    return super.put (sName, sValue);
  }

  @Deprecated
  @Override
  @DevelopersNote ("Use add instead - only for API compliance!")
  public void putAll (final Map <? extends String, ? extends String> aMap)
  {
    super.putAll (aMap);
  }

  @Nonnull
  public SMap addIfNotNull (@Nonnull final String sName, @Nullable final String sValue)
  {
    if (sValue != null)
      super.put (sName, sValue);
    return this;
  }

  @Nonnull
  public SMap add (@Nonnull final String sName, @Nonnull final String sValue)
  {
    super.put (sName, sValue);
    return this;
  }

  @Nonnull
  public SMap add (@Nonnull final String sName, @Nonnull final Locale aLocale)
  {
    return add (sName, aLocale.toString ());
  }

  @Nonnull
  public SMap add (@Nonnull final String sName, final int nValue)
  {
    return add (sName, Integer.toString (nValue));
  }

  @Nonnull
  public SMap addAll (@Nullable final Map <String, String> aMap)
  {
    if (aMap != null)
      super.putAll (aMap);
    return this;
  }

  @Nonnull
  public SMap getClone ()
  {
    return new SMap (this);
  }
}
