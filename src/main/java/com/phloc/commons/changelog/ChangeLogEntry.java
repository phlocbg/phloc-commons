/**
 * Copyright (C) 2006-2014 phloc systems
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
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.ReturnsImmutableObject;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.text.IMultiLingualText;
import com.phloc.commons.text.IReadonlyMultiLingualText;
import com.phloc.commons.text.impl.MultiLingualText;
import com.phloc.commons.text.impl.ReadonlyMultiLingualText;

/**
 * This class represents a single entry in the changelog.
 * 
 * @author Philip Helger
 */
public final class ChangeLogEntry extends AbstractChangeLogEntry
{
  private final ChangeLog m_aChangeLog;
  private final EChangeLogAction m_eAction;
  private final EChangeLogCategory m_eCategory;
  private final boolean m_bIsIncompatible;
  private final IMultiLingualText m_aTexts = new MultiLingualText ();
  private final List <String> m_aIssues = new ArrayList <String> ();

  /**
   * Constructor.
   * 
   * @param aChangeLog
   *        The owning changelog object. May not be <code>null</code>.
   * @param aDate
   *        The issue date of the change log entry. May not be <code>null</code>
   *        .
   * @param eAction
   *        The action that was performed. May not be <code>null</code>.
   * @param eCategory
   *        The category to which the entry belongs. May not be
   *        <code>null</code>.
   * @param bIsIncompatible
   *        <code>true</code> if the change has known incompatibility
   */
  public ChangeLogEntry (@Nonnull final ChangeLog aChangeLog,
                         @Nonnull final Date aDate,
                         @Nonnull final EChangeLogAction eAction,
                         @Nonnull final EChangeLogCategory eCategory,
                         final boolean bIsIncompatible)
  {
    super (aDate);
    if (aChangeLog == null)
      throw new NullPointerException ("changeLog");
    if (aDate == null)
      throw new NullPointerException ("date");
    if (eAction == null)
      throw new NullPointerException ("action");
    if (eCategory == null)
      throw new NullPointerException ("category");
    m_aChangeLog = aChangeLog;
    m_eAction = eAction;
    m_eCategory = eCategory;
    m_bIsIncompatible = bIsIncompatible;
  }

  /**
   * @return The owning changelog. Never <code>null</code>.
   */
  @Nonnull
  public ChangeLog getChangeLog ()
  {
    return m_aChangeLog;
  }

  /**
   * @return The action that was performed. Never <code>null</code>.
   */
  @Nonnull
  public EChangeLogAction getAction ()
  {
    return m_eAction;
  }

  /**
   * @return The category to which the action belongs. Never <code>null</code>.
   */
  @Nonnull
  public EChangeLogCategory getCategory ()
  {
    return m_eCategory;
  }

  /**
   * @return <code>true</code> if this changelog entry is about a change that
   *         has known incompatibility
   */
  public boolean isIncompatible ()
  {
    return m_bIsIncompatible;
  }

  /**
   * Set the change log entry display text
   * 
   * @param aMLT
   *        The text to be set. May not be <code>null</code>.
   * @return {@link EChange}
   */
  @Nonnull
  EChange setText (@Nonnull final IReadonlyMultiLingualText aMLT)
  {
    if (aMLT == null)
      throw new NullPointerException ("MLT");

    return m_aTexts.assignFrom (aMLT);
  }

  /**
   * Set the change text of this entry in the specified locale.
   * 
   * @param aContentLocale
   *        The locale of the change.
   * @param sText
   *        The text to be set. If the text is <code>null</code> or empty the
   *        call is ignored.
   * @return {@link EChange}
   */
  @Nonnull
  public EChange setText (@Nonnull final Locale aContentLocale, @Nullable final String sText)
  {
    final String sRealText = StringHelper.trim (sText);
    if (StringHelper.hasNoText (sRealText))
      return EChange.UNCHANGED;
    return m_aTexts.setText (aContentLocale, sRealText);
  }

  /**
   * @return A new multilingual text containing all change texts of this entry.
   */
  @Nonnull
  @ReturnsMutableCopy
  public IReadonlyMultiLingualText getAllTexts ()
  {
    return new ReadonlyMultiLingualText (m_aTexts);
  }

  /**
   * Get the text of the specified locale.
   * 
   * @param aContentLocale
   *        The locale to query. May not be <code>null</code>.
   * @return <code>null</code> if no such text is contained.
   */
  @Nullable
  public String getText (final Locale aContentLocale)
  {
    return m_aTexts.getTextWithLocaleFallback (aContentLocale);
  }

  /**
   * Add a new issue ID to this entry.
   * 
   * @param sIssue
   *        The issue ID to be added. If it is <code>null</code> or empty, the
   *        call is ignored.
   * @return {@link EChange}
   */
  @Nonnull
  public EChange addIssue (@Nullable final String sIssue)
  {
    final String sRealIssue = StringHelper.trim (sIssue);
    if (StringHelper.hasNoText (sRealIssue))
      return EChange.UNCHANGED;
    m_aIssues.add (sRealIssue);
    return EChange.CHANGED;
  }

  /**
   * @return An unmodifiable, non-<code>null</code> list of all contained issue
   *         IDs.
   */
  @Nonnull
  @ReturnsImmutableObject
  public List <String> getAllIssues ()
  {
    return ContainerHelper.makeUnmodifiable (m_aIssues);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!super.equals (o))
      return false;
    final ChangeLogEntry rhs = (ChangeLogEntry) o;
    return m_aChangeLog.getComponent ().equals (rhs.m_aChangeLog.getComponent ()) &&
           m_eAction.equals (rhs.m_eAction) &&
           m_eCategory.equals (rhs.m_eCategory) &&
           m_bIsIncompatible == rhs.m_bIsIncompatible &&
           m_aTexts.equals (rhs.m_aTexts) &&
           m_aIssues.equals (rhs.m_aIssues);
  }

  @Override
  public int hashCode ()
  {
    return HashCodeGenerator.getDerived (super.hashCode ())
                            .append (m_aChangeLog.getComponent ())
                            .append (m_eAction)
                            .append (m_eCategory)
                            .append (m_bIsIncompatible)
                            .append (m_aTexts)
                            .append (m_aIssues)
                            .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ())
                            .append ("changelog", m_aChangeLog.getComponent ())
                            .append ("action", m_eAction)
                            .append ("category", m_eCategory)
                            .append ("isIncompatible", m_bIsIncompatible)
                            .append ("texts", m_aTexts)
                            .append ("issues", m_aIssues)
                            .toString ();
  }
}
