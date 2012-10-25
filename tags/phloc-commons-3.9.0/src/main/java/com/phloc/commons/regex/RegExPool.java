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
package com.phloc.commons.regex;

import java.util.regex.Pattern;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.RegEx;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.cache.AbstractNotifyingCacheWithMaxSize;

/**
 * This class provides a pool for cached regular expressions. It caches up to a
 * limited number of compiled {@link Pattern} objects.
 * 
 * @author philip
 */
public final class RegExPool extends AbstractNotifyingCacheWithMaxSize <RegExPattern, Pattern>
{
  public static final int MAX_CACHE_SIZE = 1000;
  private static final RegExPool s_aInstance = new RegExPool ();

  private RegExPool ()
  {
    super (RegExPool.class.getName (), MAX_CACHE_SIZE);
  }

  @Override
  @Nullable
  protected Pattern getValueToCache (@Nullable @RegEx final RegExPattern aRegEx)
  {
    return aRegEx == null ? null : aRegEx.getAsPattern ();
  }

  /**
   * Get the cached regular expression pattern.
   * 
   * @param sRegEx
   *        The regular expression to retrieve. May neither be <code>null</code>
   *        nor empty.
   * @return The compiled regular expression pattern and never <code>null</code>
   *         .
   * @throws IllegalArgumentException
   *         If the passed regular expression has an illegal syntax
   */
  @Nonnull
  public static Pattern getPattern (@Nonnull @Nonempty @RegEx final String sRegEx)
  {
    return s_aInstance.getFromCache (new RegExPattern (sRegEx));
  }

  /**
   * Get the cached regular expression pattern.
   * 
   * @param sRegEx
   *        The regular expression to retrieve. May neither be <code>null</code>
   *        nor empty.
   * @param nOptions
   *        The options used for Pattern.compile
   * @return The compiled regular expression pattern and never <code>null</code>
   *         .
   * @see Pattern#compile(String, int)
   * @throws IllegalArgumentException
   *         If the passed regular expression has an illegal syntax
   */
  @Nonnull
  public static Pattern getPattern (@Nonnull @Nonempty @RegEx final String sRegEx, @Nonnegative final int nOptions)
  {
    return s_aInstance.getFromCache (new RegExPattern (sRegEx, nOptions));
  }
}
