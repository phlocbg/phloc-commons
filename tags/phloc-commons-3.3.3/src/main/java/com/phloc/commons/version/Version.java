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
package com.phloc.commons.version;

import java.io.Serializable;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.IHasStringRepresentation;
import com.phloc.commons.compare.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.regex.RegExHelper;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;

/**
 * This class represents a single version object. See OSGi v4 reference 3.2.4
 * Needs to be public for the JUnit test.
 *
 * @author philip
 */
@Immutable
public final class Version implements Comparable <Version>, IHasStringRepresentation, Serializable
{
  /** default version if nothing is specified. */
  public static final String DEFAULT_VERSION_STRING = "0";

  /** major version. */
  private final int m_nMajor;

  /** minor version. */
  private final int m_nMinor;

  /** micro version. */
  private final int m_nMicro;

  /** version build qualifier. */
  private final String m_sQualifier;

  /**
   * Create a new version with major version only.
   *
   * @param nMajor
   *        major version
   * @throws IllegalArgumentException
   *         if the parameter is &lt; 0
   */
  public Version (@Nonnegative final int nMajor)
  {
    this (nMajor, 0, 0, null);
  }

  /**
   * Create a new version with major and minor version only.
   *
   * @param nMajor
   *        major version
   * @param nMinor
   *        minor version
   * @throws IllegalArgumentException
   *         if any of the parameters is &lt; 0
   */
  public Version (@Nonnegative final int nMajor, @Nonnegative final int nMinor)
  {
    this (nMajor, nMinor, 0, null);
  }

  /**
   * Create a new version with major, minor and micro version number. The
   * qualifier remains null.
   *
   * @param nMajor
   *        major version
   * @param nMinor
   *        minor version
   * @param nMicro
   *        micro version
   * @throws IllegalArgumentException
   *         if any of the parameters is &lt; 0
   */
  public Version (@Nonnegative final int nMajor, @Nonnegative final int nMinor, @Nonnegative final int nMicro)
  {
    this (nMajor, nMinor, nMicro, null);
  }

  public Version (@Nonnegative final int nMajor,
                  @Nonnegative final int nMinor,
                  @Nonnegative final int nMicro,
                  @Nonnegative final int nQualifier)
  {
    this (nMajor, nMinor, nMicro, Integer.toString (nQualifier));
  }

  /**
   * Create a new version with 3 integer values and a qualifier.
   *
   * @param nMajor
   *        major version
   * @param nMinor
   *        minor version
   * @param nMicro
   *        micro version
   * @param sQualifier
   *        the version qualifier - may be null. If a qualifier is supplied, it
   *        may neither contain the "." or the "," character since they are used
   *        to determine the fields of a version and to separate 2 versions in a
   *        VersionRange.
   * @throws IllegalArgumentException
   *         if any of the numeric parameters is &lt; 0 or if the qualifier
   *         contains a forbidden character
   */
  public Version (@Nonnegative final int nMajor,
                  @Nonnegative final int nMinor,
                  @Nonnegative final int nMicro,
                  @Nullable final String sQualifier)
  {
    m_nMajor = nMajor;
    m_nMinor = nMinor;
    m_nMicro = nMicro;
    m_sQualifier = StringHelper.hasNoText (sQualifier) ? null : sQualifier;

    // check consistency
    if (m_nMajor < 0)
      throw new IllegalArgumentException ("Major version " + m_nMajor + " is < 0");
    if (m_nMinor < 0)
      throw new IllegalArgumentException ("Minor version " + m_nMinor + " is < 0");
    if (m_nMicro < 0)
      throw new IllegalArgumentException ("Micro version " + m_nMicro + " is < 0");
  }

  /**
   * Construct a version object from a string.<br>
   * EBNF:<br>
   * version ::= major( '.' minor ( '.' micro ( '.' qualifier )? )? )?<br>
   * major ::= number<br>
   * minor ::= number<br>
   * micro ::= number<br>
   * qualifier ::= ( alphanum | '_' | '-' )+
   *
   * @param sVersionString
   *        the version string to be interpreted as a version
   * @throws IllegalArgumentException
   *         if any of the parameters is &lt; 0
   */
  public Version (@Nullable final String sVersionString)
  {
    String s = sVersionString == null ? "" : sVersionString.trim ();
    if (s.length () == 0)
      s = DEFAULT_VERSION_STRING;

    // split each token
    final String [] aParts = RegExHelper.split (s, "\\.", 4);
    if (aParts.length > 0)
      m_nMajor = StringHelper.parseInt (aParts[0], 0);
    else
      m_nMajor = 0;
    if (aParts.length > 1)
      m_nMinor = StringHelper.parseInt (aParts[1], 0);
    else
      m_nMinor = 0;
    if (aParts.length > 2)
      m_nMicro = StringHelper.parseInt (aParts[2], 0);
    else
      m_nMicro = 0;
    if (aParts.length > 3)
      m_sQualifier = StringHelper.hasNoText (aParts[3]) ? null : aParts[3];
    else
      m_sQualifier = null;

    // check consistency
    if (m_nMajor < 0)
      throw new IllegalArgumentException ("Major version " + m_nMajor + " is < 0");
    if (m_nMinor < 0)
      throw new IllegalArgumentException ("Minor version " + m_nMinor + " is < 0");
    if (m_nMicro < 0)
      throw new IllegalArgumentException ("Micro version " + m_nMicro + " is < 0");
  }

  @Nonnegative
  public int getMajor ()
  {
    return m_nMajor;
  }

  @Nonnegative
  public int getMinor ()
  {
    return m_nMinor;
  }

  @Nonnegative
  public int getMicro ()
  {
    return m_nMicro;
  }

  @Nullable
  public String getQualifier ()
  {
    return m_sQualifier;
  }

  /**
   * Compares two Version objects.
   *
   * @param rhs
   *        the version to compare to
   * @return &lt; 0 if this is less than rhs; &gt; 0 if this is greater than
   *         rhs, and 0 if they are equal.
   * @throws IllegalArgumentException
   *         if the parameter is null
   */
  public int compareTo (@Nonnull final Version rhs)
  {
    if (rhs == null)
      throw new NullPointerException ("Cannot compare to null");

    // compare major version
    int ret = m_nMajor - rhs.m_nMajor;
    if (ret == 0)
    {
      // compare minor version
      ret = m_nMinor - rhs.m_nMinor;
      if (ret == 0)
      {
        // compare micro version
        ret = m_nMicro - rhs.m_nMicro;
        if (ret == 0)
        {
          // check qualifier
          if (m_sQualifier != null)
          {
            if (rhs.m_sQualifier != null)
            {
              ret = m_sQualifier.compareTo (rhs.m_sQualifier);

              // convert to -1/0/+1
              if (ret < 0)
                ret = -1;
              else
                if (ret > 0)
                  ret = +1;
            }
            else
              ret = 1;
          }
          else
            if (rhs.m_sQualifier != null)
            {
              // only this qualifier == null
              ret = -1;
            }
            else
            {
              // both qualifier are null
              ret = 0;
            }
        }
      }
    }
    return ret;
  }

  public boolean isGreaterThan (@Nonnull final Version aVersion)
  {
    return compareTo (aVersion) > 0;
  }

  public boolean isGreaterOrEqualThan (@Nonnull final Version aVersion)
  {
    return compareTo (aVersion) >= 0;
  }

  public boolean isLowerThan (@Nonnull final Version aVersion)
  {
    return compareTo (aVersion) < 0;
  }

  public boolean isLowerOrEqualThan (@Nonnull final Version aVersion)
  {
    return compareTo (aVersion) <= 0;
  }

  @Nonnull
  public String getAsString ()
  {
    final StringBuilder aSB = new StringBuilder (m_sQualifier != null ? m_sQualifier : "");
    if (m_nMicro > 0 || aSB.length () > 0)
    {
      if (aSB.length () > 0)
        aSB.insert (0, '.');
      aSB.insert (0, m_nMicro);
    }
    if (m_nMinor > 0 || aSB.length () > 0)
    {
      if (aSB.length () > 0)
        aSB.insert (0, '.');
      aSB.insert (0, m_nMinor);
    }
    if (m_nMajor > 0 || aSB.length () > 0)
    {
      if (aSB.length () > 0)
        aSB.insert (0, '.');
      aSB.insert (0, m_nMajor);
    }
    return aSB.length () > 0 ? aSB.toString () : DEFAULT_VERSION_STRING;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof Version))
      return false;
    final Version rhs = (Version) o;
    return m_nMajor == rhs.m_nMajor &&
           m_nMinor == rhs.m_nMinor &&
           m_nMicro == rhs.m_nMicro &&
           EqualsUtils.nullSafeEquals (m_sQualifier, rhs.m_sQualifier);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_nMajor)
                                       .append (m_nMinor)
                                       .append (m_nMicro)
                                       .append (m_sQualifier)
                                       .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("major", m_nMajor)
                                       .append ("minor", m_nMinor)
                                       .append ("micro", m_nMicro)
                                       .appendIfNotNull ("qualifier", m_sQualifier)
                                       .toString ();
  }
}
