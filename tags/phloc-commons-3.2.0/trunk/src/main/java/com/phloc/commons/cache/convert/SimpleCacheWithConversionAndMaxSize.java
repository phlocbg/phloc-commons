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

import java.util.Map;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.cache.LoggingLRUCache;

/**
 * A special cache that can create the value to be cache automatically from the
 * key. It also has an upper limit of elements that can reside inside the cache.
 * 
 * @author philip
 * @param <K>
 *        Cache key type
 * @param <V>
 *        Cache value type
 */
@NotThreadSafe
public class SimpleCacheWithConversionAndMaxSize <K, V> extends SimpleCacheWithConversion <K, V>
{
  private final int m_nMaxSize;

  public SimpleCacheWithConversionAndMaxSize (@Nonnull final String sCacheName, @Nonnegative final int nMaxSize)
  {
    super (sCacheName);
    if (nMaxSize <= 0)
      throw new IllegalArgumentException ("MaxSize must be > 0!");
    m_nMaxSize = nMaxSize;
  }

  /**
   * @return The maximum number of entries in this cache. Always &gt; 0.
   */
  @Nonnegative
  public final int getMaxSize ()
  {
    return m_nMaxSize;
  }

  @Override
  @Nonnull
  protected final Map <K, V> createCache ()
  {
    return new LoggingLRUCache <K, V> (getName (), m_nMaxSize);
  }
}
