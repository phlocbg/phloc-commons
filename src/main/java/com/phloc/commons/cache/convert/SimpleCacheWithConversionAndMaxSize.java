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
package com.phloc.commons.cache.convert;

import java.util.Map;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

import com.phloc.commons.cache.LoggingLRUCache;
import com.phloc.commons.string.ToStringGenerator;

/**
 * A special cache that can create the value to be cache automatically from the
 * key. It also has an upper limit of elements that can reside inside the cache.
 * 
 * @author philip
 * @param <KEYTYPE>
 *        Cache key type
 * @param <VALUETYPE>
 *        Cache value type
 */
@ThreadSafe
public class SimpleCacheWithConversionAndMaxSize <KEYTYPE, VALUETYPE> extends SimpleCacheWithConversion <KEYTYPE, VALUETYPE>
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
  protected final Map <KEYTYPE, VALUETYPE> createCache ()
  {
    return new LoggingLRUCache <KEYTYPE, VALUETYPE> (getName (), m_nMaxSize);
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ()).append ("maxSize", m_nMaxSize).toString ();
  }
}
