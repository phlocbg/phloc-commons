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
package com.phloc.commons.text.resolve;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.GlobalDebug;
import com.phloc.commons.annotations.ReturnsImmutableObject;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.locale.LocaleUtils;
import com.phloc.commons.stats.IStatisticsHandlerKeyedCounter;
import com.phloc.commons.stats.StatisticsManager;
import com.phloc.commons.text.resource.ResourceBundleUtils;

/**
 * Text resolving class that performs the fallback handling for locales other
 * than German and English. Used only from within the
 * {@link DefaultTextResolver} static class.
 * 
 * @author philip
 */
@ThreadSafe
public final class EnumTextResolverWithPropertiesOverrideAndFallback extends
                                                                    AbstractEnumTextResolverWithOverrideAndFallback
{
  public static final String PREFIX_OVERRIDE = "properties/override-";
  public static final String PREFIX_FALLBACK = "properties/";

  private static final Logger s_aLogger = LoggerFactory.getLogger (EnumTextResolverWithPropertiesOverrideAndFallback.class);
  private static final IStatisticsHandlerKeyedCounter s_aStatsFailed = StatisticsManager.getKeyedCounterHandler (EnumTextResolverWithPropertiesOverrideAndFallback.class.getName () +
                                                                                                                 "$failed");

  private final ReadWriteLock m_aRWLock = new ReentrantReadWriteLock ();
  private final Map <String, Boolean> m_aUsedOverrideBundles = new HashMap <String, Boolean> ();
  private final Map <String, Boolean> m_aUsedFallbackBundles = new HashMap <String, Boolean> ();

  @Override
  @Nullable
  protected String getOverrideString (final String sID, @Nonnull final Locale aContentLocale)
  {
    // Try all possible locales of the passed locale
    for (final Locale aLocale : LocaleUtils.getCalculatedLocaleListForResolving (aContentLocale))
    {
      // Explicitly use a bundle name containing the locale in the base name to
      // avoid strange fallback behaviour to the default locale
      final String sBundleName = PREFIX_OVERRIDE + aLocale.toString ();
      final String ret = ResourceBundleUtils.getString (sBundleName, aLocale, sID);
      if (ret != null)
      {
        // Match!
        m_aRWLock.writeLock ().lock ();
        try
        {
          m_aUsedOverrideBundles.put (sBundleName, Boolean.TRUE);
        }
        finally
        {
          m_aRWLock.writeLock ().unlock ();
        }
        return ret;
      }
    }
    return null;
  }

  @Override
  @Nullable
  protected String getFallbackString (final String sID, final Locale aContentLocale)
  {
    // Try all possible locales of the passed locale
    for (final Locale aLocale : LocaleUtils.getCalculatedLocaleListForResolving (aContentLocale))
    {
      // Explicitly use a bundle name containing the locale in the base name to
      // avoid strange fallback behaviour to the default locale
      final String sBundleName = PREFIX_FALLBACK + aLocale.toString ();
      final String ret = ResourceBundleUtils.getString (sBundleName, aLocale, sID);
      if (ret != null)
      {
        m_aRWLock.writeLock ().lock ();
        try
        {
          m_aUsedFallbackBundles.put (sBundleName, Boolean.TRUE);
        }
        finally
        {
          m_aRWLock.writeLock ().unlock ();
        }
        return ret;
      }
    }

    s_aStatsFailed.increment (PREFIX_FALLBACK + aContentLocale.toString () + ':' + sID);
    if (GlobalDebug.isDebugMode ())
    {
      s_aLogger.warn ("getFallbackString (" + sID + "; " + aContentLocale + ") failed!");
      return "[fallback-" + sID.substring (sID.lastIndexOf ('.') + 1) + "-" + aContentLocale.toString () + "]";
    }
    return null;
  }

  /**
   * @return A set with all resource keys used in overriding. Never
   *         <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  public Set <String> getAllUsedOverrideBundleNames ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return ContainerHelper.newSet (m_aUsedOverrideBundles.keySet ());
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * @return A set with all resource keys used as fallback. Never
   *         <code>null</code>.
   */
  @Nonnull
  @ReturnsImmutableObject
  public Set <String> getAllUsedFallbackBundleNames ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return ContainerHelper.newSet (m_aUsedFallbackBundles.keySet ());
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  public void clearCache ()
  {
    m_aRWLock.writeLock ().lock ();
    try
    {
      ResourceBundleUtils.clearCache ();
      m_aUsedOverrideBundles.clear ();
      m_aUsedFallbackBundles.clear ();
      s_aLogger.info ("Cache was cleared: " + getClass ().getName ());
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }
}
