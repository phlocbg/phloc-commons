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
package com.phloc.commons.convert;

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * An implementation of {@link IUnidirectionalConverter} that converts from a
 * map key to a map value
 * 
 * @author Philip Helger
 * @param <KEYTYPE>
 *        Map key type
 * @param <VALUETYPE>
 *        Map value type
 */
public final class UnidirectionalConverterMapGet <KEYTYPE, VALUETYPE> implements IUnidirectionalConverter <KEYTYPE, VALUETYPE>
{
  private final Map <KEYTYPE, VALUETYPE> m_aMap;

  public UnidirectionalConverterMapGet (@Nonnull final Map <KEYTYPE, VALUETYPE> aMap)
  {
    if (aMap == null)
      throw new NullPointerException ("map");
    m_aMap = aMap;
  }

  @Nullable
  public VALUETYPE convert (@Nullable final KEYTYPE aKey)
  {
    return m_aMap.get (aKey);
  }
}
