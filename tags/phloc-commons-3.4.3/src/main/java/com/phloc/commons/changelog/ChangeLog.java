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
package com.phloc.commons.changelog;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsImmutableObject;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.version.Version;

/**
 * This class represents a single change log with a list of entries and
 * releases.
 * 
 * @author philip
 */
public final class ChangeLog
{
  private final Version m_aVersion;
  private final String m_sComponent;
  private final List <ChangeLogEntry> m_aEntries = new ArrayList <ChangeLogEntry> ();
  private final List <ChangeLogRelease> m_aReleases = new ArrayList <ChangeLogRelease> ();

  /**
   * Constructor.
   * 
   * @param aVersion
   *        The change log version.
   * @param sComponent
   *        The name of the component the changelog belongs to.
   */
  public ChangeLog (@Nonnull final Version aVersion, @Nonnull @Nonempty final String sComponent)
  {
    if (aVersion == null)
      throw new NullPointerException ("version");
    if (StringHelper.hasNoText (sComponent))
      throw new IllegalArgumentException ("component");
    m_aVersion = aVersion;
    m_sComponent = sComponent;
  }

  /**
   * @return The change log version. Never <code>null</code>.
   */
  @Nonnull
  public Version getVersion ()
  {
    return m_aVersion;
  }

  /**
   * @return The name of the component, to which this change log belongs.
   *         Neither <code>null</code> nor empty.
   */
  @Nonnull
  @Nonempty
  public String getComponent ()
  {
    return m_sComponent;
  }

  /**
   * Add a new change log entry at the end.
   * 
   * @param aEntry
   *        The entry to be added. May not be <code>null</code>.
   */
  public void addEntry (@Nonnull final ChangeLogEntry aEntry)
  {
    if (aEntry == null)
      throw new NullPointerException ("entry");
    m_aEntries.add (aEntry);
  }

  /**
   * Add a new change log entry at the specified index.
   * 
   * @param nIndex
   *        The index to add the change log entry. May not be &lt; 0.
   * @param aEntry
   *        The entry to be added. May not be <code>null</code>.
   */
  public void addEntry (@Nonnegative final int nIndex, @Nonnull final ChangeLogEntry aEntry)
  {
    if (aEntry == null)
      throw new NullPointerException ("entry");
    m_aEntries.add (nIndex, aEntry);
  }

  /**
   * @return An unmodifiable list of all change log entries. Never
   *         <code>null</code>.
   */
  @Nonnull
  @ReturnsImmutableObject
  public List <ChangeLogEntry> getAllEntries ()
  {
    return ContainerHelper.makeUnmodifiable (m_aEntries);
  }

  /**
   * Get all change log entries, that match the specified category.
   * 
   * @param eCategory
   *        The category to search. May not be <code>null</code>.
   * @return An empty list, if no change log entry matched the specified
   *         category. Never <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  public List <ChangeLogEntry> getAllEntriesOfCategory (@Nonnull final EChangeLogCategory eCategory)
  {
    if (eCategory == null)
      throw new NullPointerException ("category");

    final List <ChangeLogEntry> ret = new ArrayList <ChangeLogEntry> ();
    for (final ChangeLogEntry aEntry : m_aEntries)
      if (aEntry.getCategory ().equals (eCategory))
        ret.add (aEntry);
    return ret;
  }

  /**
   * Add a new release at the end.
   * 
   * @param aRelease
   *        The release to be added. May not be <code>null</code>.
   */
  public void addRelease (@Nonnull final ChangeLogRelease aRelease)
  {
    if (aRelease == null)
      throw new NullPointerException ("release");
    m_aReleases.add (aRelease);
  }

  /**
   * Add a new release at the specified index.
   * 
   * @param nIndex
   *        The index to add the release. May not be &lt; 0.
   * @param aRelease
   *        The release to be added. May not be <code>null</code>.
   */
  public void addRelease (@Nonnegative final int nIndex, @Nonnull final ChangeLogRelease aRelease)
  {
    if (aRelease == null)
      throw new NullPointerException ("release");
    m_aReleases.add (nIndex, aRelease);
  }

  /**
   * @return A list of all contained releases in this change log.
   */
  @Nonnull
  @ReturnsImmutableObject
  public List <ChangeLogRelease> getAllReleases ()
  {
    return ContainerHelper.makeUnmodifiable (m_aReleases);
  }

  /**
   * @return The release with the latest date. May be <code>null</code> if no
   *         release is contained.
   */
  @Nullable
  public ChangeLogRelease getLatestRelease ()
  {
    ChangeLogRelease aLatest = null;
    for (final ChangeLogRelease aRelease : m_aReleases)
      if (aLatest == null || aRelease.getDate ().getTime () > aLatest.getDate ().getTime ())
        aLatest = aRelease;
    return aLatest;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof ChangeLog))
      return false;
    final ChangeLog rhs = (ChangeLog) o;
    return m_aVersion.equals (rhs.m_aVersion) &&
           m_sComponent.equals (rhs.m_sComponent) &&
           m_aEntries.equals (rhs.m_aEntries) &&
           m_aReleases.equals (rhs.m_aReleases);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aVersion)
                                       .append (m_sComponent)
                                       .append (m_aEntries)
                                       .append (m_aReleases)
                                       .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("version", m_aVersion)
                                       .append ("component", m_sComponent)
                                       .append ("entries", m_aEntries)
                                       .append ("releases", m_aReleases)
                                       .toString ();
  }
}
