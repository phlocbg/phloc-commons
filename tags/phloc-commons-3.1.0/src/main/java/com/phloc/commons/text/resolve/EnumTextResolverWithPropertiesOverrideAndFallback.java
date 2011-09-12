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
package com.phloc.commons.text.resolve;

import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.GlobalDebug;
import com.phloc.commons.annotations.ReturnsImmutableObject;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.stats.IStatisticsHandlerKeyedCounter;
import com.phloc.commons.stats.StatisticsManager;
import com.phloc.commons.text.resource.ResourceBundleKey;

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
  private final ConcurrentHashMap <String, Boolean> m_aUsedOverrideBundles = new ConcurrentHashMap <String, Boolean> ();
  private final ConcurrentHashMap <String, Boolean> m_aUsedFallbackBundles = new ConcurrentHashMap <String, Boolean> ();

  @Override
  @Nullable
  protected String getOverrideString (final String sID, final Locale aContentLocale)
  {
    final String sBundleName = PREFIX_OVERRIDE + aContentLocale.toString ();
    final String ret = ResourceBundleKey.getString (sBundleName, aContentLocale, sID);
    if (ret != null)
      m_aUsedOverrideBundles.put (sBundleName, Boolean.TRUE);
    return ret;
  }

  @Override
  @Nullable
  protected String getFallbackString (final String sID, final Locale aContentLocale)
  {
    final String sBundleName = PREFIX_FALLBACK + aContentLocale.toString ();
    String ret = ResourceBundleKey.getString (sBundleName, aContentLocale, sID);
    if (ret == null)
    {
      s_aStatsFailed.increment (sBundleName + ':' + sID);
      if (GlobalDebug.isDebugMode ())
      {
        s_aLogger.warn ("getFallbackString (" + sID + "; " + aContentLocale + ") failed!");
        ret = "[fallback-" + sID.substring (sID.lastIndexOf ('.') + 1) + "-" + aContentLocale.toString () + "]";
      }
    }
    else
      m_aUsedFallbackBundles.put (sBundleName, Boolean.TRUE);
    return ret;
  }

  @Nonnull
  @ReturnsImmutableObject
  public Set <String> getAllUsedOverrideBundleNames ()
  {
    return ContainerHelper.makeUnmodifiable (m_aUsedOverrideBundles.keySet ());
  }

  @Nonnull
  @ReturnsImmutableObject
  public Set <String> getAllUsedFallbackBundleNames ()
  {
    return ContainerHelper.makeUnmodifiable (m_aUsedFallbackBundles.keySet ());
  }

  public void clearCache ()
  {
    ResourceBundleKey.clearCache ();
    m_aUsedOverrideBundles.clear ();
    m_aUsedFallbackBundles.clear ();
    s_aLogger.info ("Cache was cleared: " + getClass ().getName ());
  }
}
