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
package com.phloc.commons.cache.convert;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.cache.AbstractCache;
import com.phloc.commons.convert.IUnidirectionalConverter;

/**
 * A special cache that can create the value to be cache automatically from the
 * key.
 * 
 * @author philip
 * @param <K>
 *        Cache key type
 * @param <V>
 *        Cache value type
 */
@NotThreadSafe
public class SimpleCacheWithConversion <K, V> extends AbstractCache <K, V>
{
  public SimpleCacheWithConversion (@Nonnull final String sCacheName)
  {
    super (sCacheName);
  }

  /**
   * Get the value from the cache. If no value is yet in the cache, the passed
   * converter is used to get the value to cache.
   * 
   * @param aKey
   *        The key of the cached object. May not be <code>null</code>.
   * @param aValueRetriever
   *        The converter to be used to retrieve the object to cache. May not be
   *        <code>null</code>. This converter may not return <code>null</code>
   *        objects to cache!
   * @return The cached value. Never <code>null</code>.
   */
  @Nonnull
  public final V getFromCache (@Nonnull final K aKey, @Nonnull final IUnidirectionalConverter <K, V> aValueRetriever)
  {
    // Already in the cache?
    V aValue = super.getFromCache (aKey);
    if (aValue == null)
    {
      // Get the value to cache
      aValue = aValueRetriever.convert (aKey);

      // We cannot cache null values!
      if (aValue == null)
        throw new IllegalStateException ("The converter returned a null object for the key '" + aKey + "'");
      putInCache (aKey, aValue);
    }
    return aValue;
  }
}
