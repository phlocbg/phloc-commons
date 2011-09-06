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
package com.phloc.commons.regex;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.annotation.Nonnull;
import javax.annotation.RegEx;

import com.phloc.commons.GlobalDebug;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.cache.AbstractNotifyingCacheWithMaxSize;
import com.phloc.commons.string.StringHelper;

/**
 * This class provides a pool for cached regular expressions. It caches up to a
 * limited number of compiled {@link Pattern} objects.
 * 
 * @author philip
 */
public final class RegExPool extends AbstractNotifyingCacheWithMaxSize <String, Pattern>
{
  public static final int MAX_CACHE_SIZE = 1000;
  private static final RegExPool s_aInstance = new RegExPool ();

  private RegExPool ()
  {
    super (RegExPool.class.getName (), MAX_CACHE_SIZE);
  }

  private static void _checkPatternConsistency (@Nonnull @RegEx final String sRegEx)
  {
    // Check if a '$' is escaped if no digits follow
    int nIndex = 0;
    while (nIndex >= 0)
    {
      nIndex = sRegEx.indexOf ('$', nIndex);
      if (nIndex != -1)
      {
        // Is the "$" followed by an int (would indicate a replacement group)
        if (!StringHelper.isInt (sRegEx.substring (nIndex + 1)))
        {
          if (nIndex + 1 < sRegEx.length () && sRegEx.charAt (nIndex + 1) == ')')
          { // NOPMD
            // "$" is the last char in a group "(...$)"
          }
          else
            if (nIndex > 0 && sRegEx.charAt (nIndex - 1) == '\\')
            { // NOPMD
              // '$' is quoted
            }
            else
              if (nIndex == sRegEx.length () - 1)
              { // NOPMD
                // '$' at end of String is OK!
              }
              else
                throw new IllegalArgumentException ("The passed regex '" + sRegEx + "' contains an unquoted '$' sign!");
        }
        nIndex++;
      }
    }
  }

  @Override
  @Nonnull
  protected Pattern getValueToCache (@Nonnull @RegEx final String sRegEx)
  {
    try
    {
      if (GlobalDebug.isDebugMode ())
        _checkPatternConsistency (sRegEx);
      return Pattern.compile (sRegEx);
    }
    catch (final PatternSyntaxException ex)
    {
      throw new IllegalArgumentException ("Passed regular expression '" + sRegEx + "' is illegal", ex);
    }
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
    if (StringHelper.hasNoText (sRegEx))
      throw new IllegalArgumentException ("regex is empty");

    return s_aInstance.getFromCache (sRegEx);
  }
}
