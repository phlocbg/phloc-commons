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
package com.phloc.commons.changelog;

import java.util.Date;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.version.Version;

/**
 * This class represents a single release within a changelog.
 * 
 * @author philip
 */
@Immutable
public final class ChangeLogRelease
{
  private final Date m_aDate;
  private final Version m_aVersion;

  /**
   * Constructor.
   * 
   * @param aDate
   *        The release date. May not be <code>null</code>.
   * @param aVersion
   *        The release version. May not be <code>null</code>.
   */
  public ChangeLogRelease (@Nonnull final Date aDate, @Nonnull final Version aVersion)
  {
    if (aDate == null)
      throw new NullPointerException ("date");
    if (aVersion == null)
      throw new NullPointerException ("version");
    m_aDate = (Date) aDate.clone ();
    m_aVersion = aVersion;
  }

  /**
   * @return A mutable, non-<code>null</code> clone of the contained release
   *         date.
   */
  @Nonnull
  @ReturnsMutableCopy
  public Date getDate ()
  {
    return (Date) m_aDate.clone ();
  }

  /**
   * @return The non-<code>null</code> version of this release.
   */
  @Nonnull
  public Version getVersion ()
  {
    return m_aVersion;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof ChangeLogRelease))
      return false;
    final ChangeLogRelease rhs = (ChangeLogRelease) o;
    return m_aDate.equals (rhs.m_aDate) && m_aVersion.equals (rhs.m_aVersion);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aDate).append (m_aVersion).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("date", m_aDate).append ("version", m_aVersion).toString ();
  }
}
