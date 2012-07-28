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
import java.util.regex.PatternSyntaxException;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.RegEx;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.GlobalDebug;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.StringParser;
import com.phloc.commons.string.ToStringGenerator;

/**
 * This class encapsulates a String and a set of options to be used in Pattern
 * compilation
 * 
 * @author philip
 */
@Immutable
public final class RegExPattern
{
  private final String m_sRegEx;
  private final int m_nOptions;
  private Pattern m_aPattern;

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
        if (!StringParser.isInt (sRegEx.substring (nIndex + 1)))
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

  public RegExPattern (@Nonnull @Nonempty @RegEx final String sRegEx)
  {
    // Default: no options
    this (sRegEx, 0);
  }

  public RegExPattern (@Nonnull @Nonempty @RegEx final String sRegEx, @Nonnegative final int nOptions)
  {
    if (StringHelper.hasNoText (sRegEx))
      throw new IllegalArgumentException ("regEx is empty");
    if (nOptions < 0)
      throw new IllegalArgumentException ("Options may not be < 0: " + nOptions);
    m_sRegEx = sRegEx;
    m_nOptions = nOptions;

    if (GlobalDebug.isDebugMode ())
      _checkPatternConsistency (sRegEx);

    try
    {
      m_aPattern = Pattern.compile (m_sRegEx, m_nOptions);
    }
    catch (final PatternSyntaxException ex)
    {
      throw new IllegalArgumentException ("Regular expression '" +
                                          m_sRegEx +
                                          "' is illegal" +
                                          (m_nOptions == 0 ? "" : " with options " + m_nOptions), ex);
    }
  }

  @Nonnull
  @Nonempty
  @RegEx
  public String getRegEx ()
  {
    return m_sRegEx;
  }

  @Nonnegative
  public int getOptions ()
  {
    return m_nOptions;
  }

  @Nonnull
  public Pattern getAsPattern ()
  {
    return m_aPattern;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof RegExPattern))
      return false;
    final RegExPattern rhs = (RegExPattern) o;
    // m_aPattern is a state variable
    return m_sRegEx.equals (rhs.m_sRegEx) && m_nOptions == rhs.m_nOptions;
  }

  @Override
  public int hashCode ()
  {
    // m_aPattern is a state variable
    return new HashCodeGenerator (this).append (m_sRegEx).append (m_nOptions).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("regex", m_sRegEx).append ("options", m_nOptions).toString ();
  }
}
