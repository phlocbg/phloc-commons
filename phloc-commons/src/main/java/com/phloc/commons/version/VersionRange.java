/**
 * Copyright (C) 2006-2015 phloc systems
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.IHasStringRepresentation;
import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;

/**
 * This class represents a range of versions. Each range needs at least a lower
 * bound but can as well have an upper bound. See OSGi v4 reference 3.2.5
 * 
 * @author Philip Helger
 */
@Immutable
public final class VersionRange implements Comparable <VersionRange>, IHasStringRepresentation, Serializable
{
  /**
   * Default version range string.
   */
  public static final String DEFAULT_VERSION_RANGE_STRING = "[0)";

  /**
   * &lt;= instead of &lt; ???
   */
  private final boolean m_bIncludeFloor;

  /**
   * floor version
   */
  private final Version m_aFloorVersion;

  /**
   * &gt;= instead of &gt; ???
   */
  private final boolean m_bIncludeCeil;

  /**
   * ceiling version
   */
  private final Version m_aCeilVersion;

  /**
   * Construct a version range object from a string.<br>
   * Examples:<br>
   * <ul>
   * <li>[1.2.3, 4.5.6) -- 1.2.3 &lt;= x &lt; 4.5.6</li>
   * <li>[1.2.3, 4.5.6] -- 1.2.3 &lt;= x &lt;= 4.5.6</li>
   * <li>(1.2.3, 4.5.6) -- 1.2.3 &lt; x &lt; 4.5.6</li>
   * <li>(1.2.3, 4.5.6] -- 1.2.3 &lt; x &lt;= 4.5.6</li>
   * <li>1.2.3 -- 1.2.3 &lt;= x</li>
   * <li>[1.2.3 -- 1.2.3 &lt;= x</li>
   * <li>(1.2.3 -- 1.2.3 &lt; x</li>
   * <li><i>null</i> -- 0.0.0 &lt;= x</li>
   * <li>1, 4 -- 1 &lt;= x &lt;= 4</li>
   * </ul>
   * 
   * @param sVersionString
   *        the version range in a string format as depicted above
   * @throws IllegalArgumentException
   *         if the floor version is &lt; than the ceiling version
   */
  public VersionRange (@Nullable final String sVersionString)
  {
    final String s = sVersionString == null ? "" : sVersionString.trim ();

    if (s.length () == 0)
    {
      // empty string == range [0.0, infinity)
      this.m_bIncludeFloor = true;
      this.m_aFloorVersion = new Version (Version.DEFAULT_VERSION_STRING);
      this.m_bIncludeCeil = false;
      this.m_aCeilVersion = null;
    }
    else
    {
      int i = 0;
      // parse initial token
      if (s.charAt (i) == '[')
      {
        this.m_bIncludeFloor = true;
        i++;
      }
      else
        if (s.charAt (i) == '(')
        {
          this.m_bIncludeFloor = false;
          i++;
        }
        else
          this.m_bIncludeFloor = true;

      // check last token
      int j = 0;
      if (StringHelper.endsWith (s, ']'))
      {
        this.m_bIncludeCeil = true;
        j++;
      }
      else
        if (StringHelper.endsWith (s, ')'))
        {
          this.m_bIncludeCeil = false;
          j++;
        }
        else
          this.m_bIncludeCeil = false;

      // get length of version stuff
      final int nRestLen = s.length () - i - j;
      if (nRestLen == 0)
      {
        // only delimiter braces present?
        this.m_aFloorVersion = new Version (Version.DEFAULT_VERSION_STRING);
        this.m_aCeilVersion = null;
      }
      else
      {
        final String [] parts = StringHelper.getExplodedArray (',', s.substring (i, s.length () - j));
        final String sFloor = parts[0].trim ();
        final String sCeiling = parts.length > 1 ? parts[1].trim () : null;

        // get floor version
        this.m_aFloorVersion = new Version (sFloor);

        if (StringHelper.hasNoText (sCeiling))
          this.m_aCeilVersion = null;
        else
          this.m_aCeilVersion = new Version (sCeiling);
      }
    }

    // check if floor <= ceil
    if (this.m_aCeilVersion != null && this.m_aFloorVersion.compareTo (this.m_aCeilVersion) > 0)
      throw new IllegalArgumentException ("Floor version may not be greater than the ceiling version!");
  }

  /**
   * Create a new version range depicted by two versions, assuming that both the
   * floor and the ceiling version should be included meaning we have an
   * inclusive interval.
   * 
   * @param aFloorVersion
   *        the floor version of the range - may not be null
   * @param aCeilingVersion
   *        the ceiling version of the range - may be null
   * @throws IllegalArgumentException
   *         if the floor version to be used is &gt; the ceiling version or if
   *         the floor version is null.
   */
  public VersionRange (@Nonnull final Version aFloorVersion, @Nullable final Version aCeilingVersion)
  {
    this (aFloorVersion, true, aCeilingVersion, true);
  }

  /**
   * Create a new version range depicted by two versions.
   * 
   * @param aFloorVersion
   *        the floor version of the range - may not be null
   * @param bIncludeFloorVersion
   *        if true, a &gt;= comparison is used on the version number, else a
   *        &gt; comparison is used
   * @param aCeilingVersion
   *        the ceiling version of the range - may be null
   * @param bIncludeCeilingVersion
   *        if true, a &lt;= comparison is used on the version number, else a
   *        &lt; comparison is used
   * @throws IllegalArgumentException
   *         if the floor version to be used is &gt; the ceiling version or if
   *         the floor version is null.
   */
  public VersionRange (@Nonnull final Version aFloorVersion,
                       final boolean bIncludeFloorVersion,
                       @Nullable final Version aCeilingVersion,
                       final boolean bIncludeCeilingVersion)
  {
    ValueEnforcer.notNull (aFloorVersion, "FloorVersion");

    // set values
    this.m_aFloorVersion = aFloorVersion;
    this.m_bIncludeFloor = bIncludeFloorVersion;
    this.m_aCeilVersion = aCeilingVersion;
    this.m_bIncludeCeil = bIncludeCeilingVersion;

    // check if floor <= ceil
    if (this.m_aCeilVersion != null && this.m_aFloorVersion.compareTo (this.m_aCeilVersion) > 0)
      throw new IllegalArgumentException ("Floor version may not be greater than the ceiling version!");
  }

  public boolean isIncludingFloor ()
  {
    return this.m_bIncludeFloor;
  }

  @Nullable
  public Version getFloorVersion ()
  {
    return this.m_aFloorVersion;
  }

  public boolean isIncludingCeil ()
  {
    return this.m_bIncludeCeil;
  }

  @Nullable
  public Version getCeilVersion ()
  {
    return this.m_aCeilVersion;
  }

  public boolean versionMatches (@Nonnull final Version rhs)
  {
    // returns -1 if floor < rhs
    // -> error
    int i = this.m_aFloorVersion.compareTo (rhs);
    if (this.m_bIncludeFloor ? i > 0 : i >= 0)
      return false;

    // check ceiling version
    if (this.m_aCeilVersion != null)
    {
      i = this.m_aCeilVersion.compareTo (rhs);
      if (this.m_bIncludeCeil ? i < 0 : i <= 0)
        return false;
    }

    return true;
  }

  /**
   * Compare this version range to another version range. Returns -1 if this is
   * &lt; than the passed version or +1 if this is &gt; the passed version range
   * 
   * @param rhs
   *        the version range to compare to
   * @return 0 if the passed version range is equal to this version range<br>
   *         -1 if the floor version of this is &lt; than the floor version of
   *         the passed version range.<br>
   *         -1 if the floor versions are equal but the ceiling version of this
   *         has a lower upper bound than the passed version range<br>
   *         +1 if the floor version of this is &gt; than the floor version of
   *         the passed version range.<br>
   *         +1 if the floor versions are equal but the ceiling version of this
   *         has a higher upper bound than the passed version range<br>
   */
  @Override
  public int compareTo (@Nonnull final VersionRange rhs)
  {
    int i = this.m_aFloorVersion.compareTo (rhs.m_aFloorVersion);
    if (i == 0)
    {
      if (this.m_bIncludeFloor && !rhs.m_bIncludeFloor)
      {
        // this < rhs
        i = -1;
      }
      else
        if (!this.m_bIncludeFloor && rhs.m_bIncludeFloor)
        {
          // this > rhs
          i = +1;
        }

      if (i == 0)
      {
        // compare ceiling
        if (this.m_aCeilVersion != null && rhs.m_aCeilVersion == null)
          i = -1;
        else
          if (this.m_aCeilVersion == null && rhs.m_aCeilVersion != null)
            i = +1;
          else
            if (this.m_aCeilVersion != null && rhs.m_aCeilVersion != null)
              i = this.m_aCeilVersion.compareTo (rhs.m_aCeilVersion);
        // else i stays 0 if both are null

        if (i == 0)
        {
          if (this.m_bIncludeCeil && !rhs.m_bIncludeCeil)
            i = +1;
          else
            if (!this.m_bIncludeCeil && rhs.m_bIncludeCeil)
              i = -1;
        }
      }
    }
    return i;
  }

  /**
   * Converts the version range to a string. The brackets whether floor or
   * ceiling version should be included or not is always prepended and appended.
   * If a ceiling version is present, the ceiling version is appended with a
   * single comma as a delimiter.<br>
   * Example return: "[1.2.3,4.5.6)"
   * 
   * @return The version range in a parseable string format.
   */
  @Override
  @Nonnull
  public String getAsString ()
  {
    return getAsString (Version.DEFAULT_PRINT_ZERO_ELEMENTS);
  }

  /**
   * Converts the version range to a string. The brackets whether floor or
   * ceiling version should be included or not is always prepended and appended.
   * If a ceiling version is present, the ceiling version is appended with a
   * single comma as a delimiter.<br>
   * Example return: "[1.2.3,4.5.6)"
   * 
   * @param bPrintZeroElements
   *        If <code>true</code> than trailing zeroes are printed, otherwise
   *        printed zeroes are not printed.
   * @return Never <code>null</code>.
   */
  @Nonnull
  public String getAsString (final boolean bPrintZeroElements)
  {
    // special handling if no ceiling version is present
    final StringBuilder aSB = new StringBuilder (this.m_bIncludeFloor ? "[" : "(");
    aSB.append (this.m_aFloorVersion.getAsString (bPrintZeroElements));
    if (this.m_aCeilVersion != null)
    {
      aSB.append (',').append (this.m_aCeilVersion.getAsString (bPrintZeroElements));
    }
    return aSB.append (this.m_bIncludeCeil ? ']' : ')').toString ();
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof VersionRange))
      return false;

    final VersionRange rhs = (VersionRange) o;
    return this.m_bIncludeFloor == rhs.m_bIncludeFloor &&
           this.m_aFloorVersion.equals (rhs.m_aFloorVersion) &&
           this.m_bIncludeCeil == rhs.m_bIncludeCeil &&
           EqualsUtils.equals (this.m_aCeilVersion, rhs.m_aCeilVersion);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (this.m_aFloorVersion)
                                       .append (this.m_bIncludeFloor)
                                       .append (this.m_aCeilVersion)
                                       .append (this.m_bIncludeCeil)
                                       .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("floorVersion", this.m_aFloorVersion)
                                       .append ("inclFloor", this.m_bIncludeFloor)
                                       .append ("ceilVersion", this.m_aCeilVersion)
                                       .append ("inclCeil", this.m_bIncludeCeil)
                                       .toString ();
  }
}
